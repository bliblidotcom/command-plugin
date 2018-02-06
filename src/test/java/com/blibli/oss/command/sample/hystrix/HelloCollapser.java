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

package com.blibli.oss.command.sample.hystrix;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCommand;

import java.util.Collection;

/**
 * @author Eko Kurniawan Khannedy
 */
public class HelloCollapser extends HystrixCollapser<String, String, String> {

  @Override
  public String getRequestArgument() {
    return null;
  }

  @Override
  protected HystrixCommand<String> createCommand(Collection<CollapsedRequest<String, String>> collapsedRequests) {
    return null;
  }

  @Override
  protected void mapResponseToRequests(String batchResponse, Collection<CollapsedRequest<String, String>> collapsedRequests) {

  }
}
