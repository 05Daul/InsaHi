package com.playdata.AttendanceSalary.atdSalDto.atd;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO<T> {

  private List<T> content;
  private int page;
  private int size;
  private int totalPages;
  private long totalElements;
}
