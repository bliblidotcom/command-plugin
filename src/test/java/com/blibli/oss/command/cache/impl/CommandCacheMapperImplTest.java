package com.blibli.oss.command.cache.impl;

import com.blibli.oss.command.exception.CommandRuntimeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandCacheMapperImplTest {

  private static final String STRING = "STRING";
  private static final String DATA = "DATA";
  private static final String VALUE = "VALUE";

  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock
  private ObjectMapper objectMapper;

  private SampleData sampleData = SampleData.builder()
      .data(DATA)
      .build();

  @InjectMocks
  private CommandCacheMapperImpl commandCacheMapper;

  @After
  public void tearDown() throws Exception {
    verifyNoMoreInteractions(objectMapper);
  }

  @Test
  public void testToStringForString() {
    String response = commandCacheMapper.toString(STRING);
    assertEquals(STRING, response);
  }

  @Test
  public void testFromStringToString() {
    String response = commandCacheMapper.fromString(STRING, String.class);
    assertEquals(STRING, response);
  }

  @Test
  public void testToString() throws JsonProcessingException {
    mockObjectMapperWriteValueAsString();
    String response = commandCacheMapper.toString(sampleData);
    assertEquals(VALUE, response);

    verify(objectMapper, times(1)).writeValueAsString(sampleData);
  }

  private void mockObjectMapperWriteValueAsString() throws JsonProcessingException {
    when(objectMapper.writeValueAsString(sampleData)).thenReturn(VALUE);
  }

  @Test(expected = CommandRuntimeException.class)
  public void testToStringError() throws JsonProcessingException {
    mockObjectMapperWriteValueAsStringError();

    try {
      commandCacheMapper.toString(sampleData);
      fail("It should error");
    } finally {
      verify(objectMapper, times(1)).writeValueAsString(sampleData);
    }
  }

  private void mockObjectMapperWriteValueAsStringError() throws JsonProcessingException {
    when(objectMapper.writeValueAsString(sampleData)).thenThrow(new CommandRuntimeException());
  }

  @Test
  public void testFromString() throws IOException {
    mockObjectMapperReadValue();
    SampleData response = commandCacheMapper.fromString(VALUE, SampleData.class);
    assertEquals(sampleData, response);

    verify(objectMapper, times(1)).readValue(VALUE, SampleData.class);
  }

  private void mockObjectMapperReadValue() throws IOException {
    when(objectMapper.readValue(VALUE, SampleData.class)).thenReturn(sampleData);
  }

  @Test(expected = CommandRuntimeException.class)
  public void testFromStringError() throws IOException {
    mockObjectMapperReadValueError();

    try {
      commandCacheMapper.fromString(VALUE, SampleData.class);
    } finally {
      verify(objectMapper, times(1)).readValue(VALUE, SampleData.class);
    }
  }

  private void mockObjectMapperReadValueError() throws IOException {
    when(objectMapper.readValue(VALUE, SampleData.class)).thenThrow(new CommandRuntimeException());
  }

  @Builder
  @Data
  private static class SampleData {

    private String data;
  }
}