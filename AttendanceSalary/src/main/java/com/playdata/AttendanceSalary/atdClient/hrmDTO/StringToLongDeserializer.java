package com.playdata.AttendanceSalary.atdClient.hrmDTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class StringToLongDeserializer extends JsonDeserializer<Long> {

  @Override
  public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    try {
      return Long.parseLong(p.getText());
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
