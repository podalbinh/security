package com.example.transactionmanagement.configs;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for ModelMapper.
 * This class defines the configuration for the ModelMapper bean used in the
 * application.
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Creates and configures a ModelMapper bean.
     * 
     * @return a configured ModelMapper instance with strict matching strategy.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
