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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.Test;

import javax.validation.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class ErrorHelperTest {

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  public void testInvalid() {
    Request request = Request.builder()
      .firstName("")
      .lastName("")
      .build();

    Set<ConstraintViolation<Request>> constraintViolations = validator.validate(request);
    Map<String, List<String>> map = ErrorHelper.from(constraintViolations);

    assertEquals("NotBlank", map.get("firstName").get(0));
    assertEquals("NotBlank", map.get("lastName").get(0));
    assertEquals("RequestValid", map.get("fullName").get(0));
  }

  @Test
  public void testValid() {
    Request request = Request.builder()
      .firstName("Eko")
      .lastName("Kurniawan")
      .build();

    Set constraintViolations = validator.validate(request);
    assertTrue(constraintViolations.isEmpty());
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @RequestValid(message = "RequestValid", path = "fullName")
  public static class Request {

    @NotBlank(message = "NotBlank")
    private String firstName;

    @NotBlank(message = "NotBlank")
    private String lastName;

  }

  @Documented
  @Constraint(validatedBy = {RequestValidator.class})
  @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE})
  @Retention(RUNTIME)
  public static @interface RequestValid {

    String message();

    String[] path() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
  }

  public static class RequestValidator implements ConstraintValidator<RequestValid, Request> {

    @Override
    public void initialize(RequestValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(Request value, ConstraintValidatorContext context) {
      return "Eko Kurniawan".equals(value.getFirstName() + " " + value.getLastName());
    }
  }

}