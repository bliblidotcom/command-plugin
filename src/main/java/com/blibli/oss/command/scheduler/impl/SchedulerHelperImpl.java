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

package com.blibli.oss.command.scheduler.impl;

import com.blibli.oss.command.properties.SchedulerProperties;
import com.blibli.oss.command.scheduler.SchedulerHelper;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class SchedulerHelperImpl implements SchedulerHelper, InitializingBean {

  private Map<String, Scheduler> schedulers = new HashMap<>();

  private SchedulerProperties schedulerProperties;

  public SchedulerHelperImpl(SchedulerProperties schedulerProperties) {
    this.schedulerProperties = schedulerProperties;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    schedulerProperties.getConfigs().forEach((name, properties) -> {
      schedulers.put(name, createScheduler(properties));
    });
  }

  private Scheduler createScheduler(SchedulerProperties.SchedulerItemProperties properties) {
    switch (properties.getType()) {
      case ELASTIC:
        return Schedulers.elastic();
      case SINGLE:
        return Schedulers.single();
      case PARALLEL:
        return Schedulers.parallel();
      case NEW_ELASTIC:
        return newElasticScheduler(properties.getNewElastic());
      case NEW_PARALLEL:
        return newParallelScheduler(properties.getNewParallel());
      case NEW_SINGLE:
        return newSingleScheduler(properties.getNewSingle());
      case EXECUTOR:
        return newExecutorScheduler(properties.getExecutor());
      case THREAD_POOL:
        return newThreadPollScheduler(properties.getThreadPool());
      case IMMEDIATE:
      default:
        return Schedulers.immediate();
    }
  }

  private Scheduler newThreadPollScheduler(SchedulerProperties.SchedulerThreadPoolProperties properties) {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
      properties.getCorePoolSize(),
      properties.getMaximumPoolSize(),
      properties.getTtlSeconds().longValue(),
      TimeUnit.SECONDS,
      createBlockingQueue(properties)
    );
    executor.allowCoreThreadTimeOut(properties.getAllowCoreThreadTimeOut());
    return Schedulers.fromExecutor(executor);
  }

  private BlockingQueue<Runnable> createBlockingQueue(SchedulerProperties.SchedulerThreadPoolProperties properties) {
    switch (properties.getQueueType()) {
      case ARRAY:
        return new ArrayBlockingQueue<>(properties.getQueueSize());
      case LINKED:
      default:
        return new LinkedBlockingQueue<>(properties.getQueueSize());
    }
  }

  private Scheduler newExecutorScheduler(SchedulerProperties.SchedulerExecutorProperties properties) {
    switch (properties.getType()) {
      case WORK_STEALING_POOL:
        return Schedulers.fromExecutorService(Executors.newWorkStealingPool(properties.getParallelism()));
      case CACHED_THREAD_POOL:
        return Schedulers.fromExecutorService(Executors.newCachedThreadPool());
      case FIXED_THREAD_POOL:
        return Schedulers.fromExecutorService(Executors.newFixedThreadPool(properties.getNumberOfThread()));
      case SINGLE_THREAD_POOL:
      default:
        return Schedulers.fromExecutorService(Executors.newSingleThreadExecutor());
    }
  }

  private Scheduler newSingleScheduler(SchedulerProperties.SchedulerNewSingleProperties properties) {
    return Schedulers.newSingle(properties.getName(), properties.getDaemon());
  }

  private Scheduler newParallelScheduler(SchedulerProperties.SchedulerNewParallelProperties properties) {
    return Schedulers.newParallel(properties.getName(), properties.getParallelism(), properties.getDaemon());
  }

  private Scheduler newElasticScheduler(SchedulerProperties.SchedulerNewElasticProperties properties) {
    return Schedulers.newElastic(properties.getName(), properties.getTtlSeconds(), properties.getDaemon());
  }

  @Override
  public Scheduler of(String name) {
    Scheduler scheduler = schedulers.get(name);
    if (scheduler == null) {
      return Schedulers.immediate();
    } else {
      return scheduler;
    }
  }
}
