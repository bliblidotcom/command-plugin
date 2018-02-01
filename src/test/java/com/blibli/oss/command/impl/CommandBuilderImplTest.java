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

package com.blibli.oss.command.impl;

import com.blibli.oss.command.Command;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandBuilderImplTest {

  private static final String REQUEST = "REQUEST";

  @Test
  public void test() {
    CommandBuilderImpl<String, String> builder = new CommandBuilderImpl<>(REQUEST, DataCommand.class);
    assertEquals(REQUEST, builder.getRequest());
    assertEquals(DataCommand.class, builder.getCommandClass());
  }

  private static interface DataCommand extends Command<String, String> {

  }
}