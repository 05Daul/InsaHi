package com.playdata.ElectronicApproval.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SetConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

}
