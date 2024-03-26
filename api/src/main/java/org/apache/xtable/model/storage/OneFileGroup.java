/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.apache.xtable.model.storage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Builder;
import lombok.Value;

import org.apache.xtable.model.stat.PartitionValue;

/** Represents a grouping of {@link InternalDataFile} with the same partition values. */
@Value
@Builder
public class OneFileGroup {
  List<PartitionValue> partitionValues;
  List<InternalDataFile> files;

  public static List<OneFileGroup> fromFiles(List<InternalDataFile> files) {
    return fromFiles(files.stream());
  }

  public static List<OneFileGroup> fromFiles(Stream<InternalDataFile> files) {
    Map<List<PartitionValue>, List<InternalDataFile>> filesGrouped =
        files.collect(Collectors.groupingBy(InternalDataFile::getPartitionValues));
    return filesGrouped.entrySet().stream()
        .map(
            entry ->
                OneFileGroup.builder()
                    .partitionValues(entry.getKey())
                    .files(entry.getValue())
                    .build())
        .collect(Collectors.toList());
  }
}
