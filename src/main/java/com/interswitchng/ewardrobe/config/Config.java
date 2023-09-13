package com.interswitchng.ewardrobe.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Configuration
@RequiredArgsConstructor
public class Config {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(STRICT);
        mapper.getConfiguration().setSkipNullEnabled(true);
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
