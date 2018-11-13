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

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import java.util.*;

/**
 * @author Eko Kurniawan Khannedy
 */
public class ErrorHelper {

  public static final String SEPARATOR = ".";
  public static final String PATH = "path";

  public static <T> Map<String, List<String>> from(Set<ConstraintViolation<T>> constraintViolations) {
    Map<String, List<String>> map = new HashMap<>(constraintViolations.size());

    constraintViolations.forEach(violation -> {
      for (String attribute : getAttributes(violation)) {
        putEntry(map, attribute, violation.getMessage());
      }
    });

    return map;
  }

  private static void putEntry(Map<String, List<String>> map, String key, String value) {
    if (!map.containsKey(key)) {
      map.put(key, new ArrayList<>());
    }
    map.get(key).add(value);
  }

  private static String[] getAttributes(ConstraintViolation<?> constraintViolation) {
    String[] values = (String[]) constraintViolation.getConstraintDescriptor().getAttributes().get(PATH);
    if (values == null || values.length == 0) {
      return getAttributesFromPath(constraintViolation);
    } else {
      return values;
    }
  }

  private static String[] getAttributesFromPath(ConstraintViolation<?> constraintViolation) {
    Path path = constraintViolation.getPropertyPath();

    StringBuilder builder = new StringBuilder();
    path.forEach(node -> {
      if (node.getName() != null) {
        if (builder.length() > 0) {
          builder.append(SEPARATOR);
        }

        builder.append(node.getName());
      }
    });

    return new String[]{builder.toString()};
  }

}
