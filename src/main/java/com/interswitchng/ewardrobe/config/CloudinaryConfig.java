package com.interswitchng.ewardrobe.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud-name}")
    private String CLOUD_NAME;
    @Value("${cloudinary.api-key}")
    private  String API_KEY;
    @Value("${cloudinary.api-secret}")
    private String API_SECRET;
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("Cloud-name",CLOUD_NAME);
        config.put("Api-key",API_KEY);
        config.put("Api-secret",API_SECRET);
        return new Cloudinary(config);
    }
}

