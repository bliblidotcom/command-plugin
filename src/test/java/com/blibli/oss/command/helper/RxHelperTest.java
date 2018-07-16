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

package com.blibli.oss.command.helper;

import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class RxHelperTest {

  @Test
  public void testSingleSuccess() {
    String value = RxHelper.single(() -> "eko").toBlocking().value();
    assertEquals("eko", value);
  }

  @Test(expected = NullPointerException.class)
  public void testSingleError() {
    RxHelper.single(() -> {
      throw new NullPointerException("ups");
    }).toBlocking().value();
  }

  @Test
  public void testObservableSuccess() {
    List<String> strings = RxHelper.observable(() ->
      Stream.of("eko", "kurniawan", "khannedy")
    ).toList().toSingle().toBlocking().value();

    assertThat(strings, hasItems("eko", "kurniawan", "khannedy"));
  }

  @Test(expected = NullPointerException.class)
  public void testObservableError() {
    RxHelper.observable(() -> {
      throw new NullPointerException("ups");
    }).toList().toSingle().toBlocking().value();
  }
}