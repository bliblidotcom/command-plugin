/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blibli.oss.command.executor;

import com.blibli.oss.command.Command;
import com.blibli.oss.command.tuple.Tuple2;
import com.blibli.oss.command.tuple.Tuple3;
import com.blibli.oss.command.tuple.Tuple4;
import com.blibli.oss.command.tuple.Tuple5;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Single;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultiCommandSingleRequestExecutorTest.Application.class)
public class MultiCommandSingleRequestExecutorTest {

  public static final String HELLO_EKO = "Hello Eko";
  public static final String BYE_EKO = "Bye Eko";

  @Autowired
  private MultiCommandSingleRequestExecutor executor;

  private HelloCommandRequest request = HelloCommandRequest.builder()
    .name("Eko")
    .build();

  @Test
  public void test2Commands() {
    Tuple2<HelloCommandResponse, HelloCommandResponse> response =
      executor.executeAll(HelloCommand.class, ByeCommand.class, request).toBlocking().value();

    assertEquals(HELLO_EKO, response.getFirst().getSay());
    assertEquals(BYE_EKO, response.getSecond().getSay());
  }

  @Test
  public void test3Commands() {
    Tuple3<HelloCommandResponse, HelloCommandResponse, HelloCommandResponse> response =
      executor.executeAll(HelloCommand.class, ByeCommand.class, HelloCommand.class, request).toBlocking().value();

    assertEquals(HELLO_EKO, response.getFirst().getSay());
    assertEquals(BYE_EKO, response.getSecond().getSay());
    assertEquals(HELLO_EKO, response.getThird().getSay());
  }

  @Test
  public void test4Commands() {
    Tuple4<HelloCommandResponse, HelloCommandResponse, HelloCommandResponse, HelloCommandResponse> response =
      executor.executeAll(HelloCommand.class, ByeCommand.class, HelloCommand.class, ByeCommand.class, request).toBlocking().value();

    assertEquals(HELLO_EKO, response.getFirst().getSay());
    assertEquals(BYE_EKO, response.getSecond().getSay());
    assertEquals(HELLO_EKO, response.getThird().getSay());
    assertEquals(BYE_EKO, response.getForth().getSay());
  }

  @Test
  public void test5Commands() {
    Tuple5<HelloCommandResponse, HelloCommandResponse, HelloCommandResponse, HelloCommandResponse, HelloCommandResponse> response =
      executor.executeAll(HelloCommand.class, ByeCommand.class, HelloCommand.class, ByeCommand.class, HelloCommand.class, request).toBlocking().value();

    assertEquals(HELLO_EKO, response.getFirst().getSay());
    assertEquals(BYE_EKO, response.getSecond().getSay());
    assertEquals(HELLO_EKO, response.getThird().getSay());
    assertEquals(BYE_EKO, response.getForth().getSay());
    assertEquals(HELLO_EKO, response.getFifth().getSay());
  }

  @SpringBootApplication
  public static class Application {

    @Bean
    public HelloCommand helloCommand() {
      return new HelloCommand();
    }

    @Bean
    public ByeCommand byeCommand() {
      return new ByeCommand();
    }

  }

  @Data
  @Builder
  public static class HelloCommandRequest {

    @NotBlank
    private String name;

  }

  @Data
  @Builder
  public static class HelloCommandResponse {

    private String say;
  }

  public static class HelloCommand implements Command<HelloCommandRequest, HelloCommandResponse> {

    @Override
    public Single<HelloCommandResponse> execute(HelloCommandRequest request) {
      return single(() -> HelloCommandResponse.builder()
        .say("Hello " + request.getName())
        .build());
    }
  }

  public static class ByeCommand implements Command<HelloCommandRequest, HelloCommandResponse> {

    @Override
    public Single<HelloCommandResponse> execute(HelloCommandRequest request) {
      return single(() -> HelloCommandResponse.builder()
        .say("Bye " + request.getName())
        .build());
    }
  }

}