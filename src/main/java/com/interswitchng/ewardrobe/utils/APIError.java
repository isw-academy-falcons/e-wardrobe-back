package com.interswitchng.ewardrobe.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor

public class APIError {
    private HttpStatus status;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime time;
    private String message;
    private String debugMessage;
    private boolean ok;

    public APIError(){time = LocalDateTime.now();}

    public APIError(HttpStatus status, Throwable ex){
        this();
        this.status =  status;
        this.message = "Unexpected Error";
        this.debugMessage = ex.getLocalizedMessage();
    }
    public APIError(HttpStatus status,String message, Throwable ex){
        this();
        this.status =  status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public String convertToJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(this);
    }
}
