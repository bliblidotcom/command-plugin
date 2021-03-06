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

package com.blibli.oss.command.sample.validation;

import com.blibli.oss.command.CommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NoValidationCommandImplTest.Application.class)
public class NoValidationCommandImplTest {

  @MockBean
  private StringRedisTemplate stringRedisTemplate;

  @Autowired
  private CommandExecutor commandExecutor;

  @MockBean
  private Validator validator;

  @Test
  public void testNoValidation() {
    NoValidationCommandResponse response = commandExecutor.execute(NoValidationCommand.class, NoValidationCommandRequest.builder().build())
        .block();

    assertNull(response.getResponse());

    verifyNoMoreInteractions(validator);
  }

  @SpringBootApplication
  public static class Application {

  }

}