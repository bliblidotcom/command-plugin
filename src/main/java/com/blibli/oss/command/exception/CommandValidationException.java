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

package com.blibli.oss.command.exception;

import com.blibli.oss.command.helper.ErrorHelper;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @author Eko Kurniawan Khannedy
 */
public class CommandValidationException extends CommandRuntimeException {

  private Set<ConstraintViolation<?>> constraintViolations;

  public CommandValidationException(Set constraintViolations) {
    this(null, constraintViolations);
  }

  public CommandValidationException(String message, Set constraintViolations) {
    super(message);
    this.constraintViolations = constraintViolations;
  }

  public Set<ConstraintViolation<?>> getConstraintViolations() {
    return constraintViolations;
  }
}
