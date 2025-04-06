package com.playdata.AttendanceSalary.setConfig;

import com.playdata.AttendanceSalary.atdSalDto.sal.AllowanceResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.DeductionResponseDTO;
import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    // AllowanceEntity → AllowxanceResponseDTO
    modelMapper.typeMap(AllowanceEntity.class, AllowanceResponseDTO.class)
        .addMappings(mapper -> {
          mapper.skip(AllowanceResponseDTO::setEmployeeId);
        });

    // DeductionEntity → DeductionResponseDTO
    modelMapper.typeMap(DeductionEntity.class, DeductionResponseDTO.class)
        .addMappings(mapper -> {
          mapper.skip(DeductionResponseDTO::setEmployeeId);
        });

    return modelMapper;
  }
}