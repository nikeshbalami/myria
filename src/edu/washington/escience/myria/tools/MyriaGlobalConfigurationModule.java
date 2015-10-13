/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.washington.escience.myria.tools;

import java.util.Set;

import org.apache.reef.tang.annotations.Name;
import org.apache.reef.tang.annotations.NamedParameter;
import org.apache.reef.tang.formats.ConfigurationModule;
import org.apache.reef.tang.formats.ConfigurationModuleBuilder;
import org.apache.reef.tang.formats.OptionalParameter;
import org.apache.reef.tang.formats.RequiredParameter;

import edu.washington.escience.myria.MyriaConstants;

public final class MyriaGlobalConfigurationModule extends ConfigurationModuleBuilder {
  public static final RequiredParameter<String> INSTANCE_NAME = new RequiredParameter<>();
  public static final RequiredParameter<String> DEFAULT_INSTANCE_PATH = new RequiredParameter<>();
  public static final RequiredParameter<String> STORAGE_DBMS = new RequiredParameter<>();
  public static final OptionalParameter<String> DEFAULT_STORAGE_DB_NAME = new OptionalParameter<>();
  public static final OptionalParameter<String> DEFAULT_STORAGE_DB_USERNAME =
      new OptionalParameter<>();
  public static final RequiredParameter<String> DEFAULT_STORAGE_DB_PASSWORD =
      new RequiredParameter<>();
  public static final OptionalParameter<Integer> DEFAULT_STORAGE_DB_PORT =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> REST_API_PORT = new OptionalParameter<>();
  public static final OptionalParameter<String> API_ADMIN_PASSWORD = new OptionalParameter<>();
  public static final OptionalParameter<Boolean> USE_SSL = new OptionalParameter<>();
  public static final OptionalParameter<String> SSL_KEYSTORE_PATH = new OptionalParameter<>();
  public static final OptionalParameter<String> SSL_KEYSTORE_PASSWORD = new OptionalParameter<>();
  public static final OptionalParameter<Boolean> ENABLE_DEBUG = new OptionalParameter<>();
  public static final RequiredParameter<String> WORKER_CONF = new RequiredParameter<>();
  public static final RequiredParameter<Integer> NUMBER_VCORES = new RequiredParameter<>();
  public static final RequiredParameter<Integer> MEMORY_QUOTA_GB = new RequiredParameter<>();
  public static final OptionalParameter<Integer> JVM_HEAP_SIZE_MIN_GB = new OptionalParameter<>();
  public static final OptionalParameter<Integer> JVM_HEAP_SIZE_MAX_GB = new OptionalParameter<>();
  public static final OptionalParameter<String> JVM_OPTIONS = new OptionalParameter<>();
  public static final OptionalParameter<Integer> FLOW_CONTROL_WRITE_BUFFER_HIGH_MARK_BYTES =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> FLOW_CONTROL_WRITE_BUFFER_LOW_MARK_BYTES =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> OPERATOR_INPUT_BUFFER_CAPACITY =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> OPERATOR_INPUT_BUFFER_RECOVER_TRIGGER =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> TCP_CONNECTION_TIMEOUT_MILLIS =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> TCP_RECEIVE_BUFFER_SIZE_BYTES =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> TCP_SEND_BUFFER_SIZE_BYTES =
      new OptionalParameter<>();
  public static final OptionalParameter<Integer> LOCAL_FRAGMENT_WORKER_THREADS =
      new OptionalParameter<>();
  public static final OptionalParameter<String> MASTER_HOST = new OptionalParameter<>();
  public static final OptionalParameter<Integer> MASTER_RPC_PORT = new OptionalParameter<>();

  public static final ConfigurationModule CONF = new MyriaGlobalConfigurationModule()
      .bindNamedParameter(InstanceName.class, INSTANCE_NAME)
      .bindNamedParameter(DefaultInstancePath.class, DEFAULT_INSTANCE_PATH)
      .bindNamedParameter(StorageDbms.class, STORAGE_DBMS)
      .bindNamedParameter(DefaultStorageDbName.class, DEFAULT_STORAGE_DB_NAME)
      .bindNamedParameter(DefaultStorageDbUser.class, DEFAULT_STORAGE_DB_USERNAME)
      .bindNamedParameter(DefaultStorageDbPassword.class, DEFAULT_STORAGE_DB_PASSWORD)
      .bindNamedParameter(DefaultStorageDbPort.class, DEFAULT_STORAGE_DB_PORT)
      .bindNamedParameter(RestApiPort.class, REST_API_PORT)
      .bindNamedParameter(ApiAdminPassword.class, API_ADMIN_PASSWORD)
      .bindNamedParameter(UseSsl.class, USE_SSL)
      .bindNamedParameter(SslKeystorePath.class, SSL_KEYSTORE_PATH)
      .bindNamedParameter(SslKeystorePassword.class, SSL_KEYSTORE_PASSWORD)
      .bindNamedParameter(EnableDebug.class, ENABLE_DEBUG)
      .bindSetEntry(WorkerConf.class, WORKER_CONF)
      .bindNamedParameter(NumberVCores.class, NUMBER_VCORES)
      .bindNamedParameter(MemoryQuotaGB.class, MEMORY_QUOTA_GB)
      .bindNamedParameter(JvmHeapSizeMinGB.class, JVM_HEAP_SIZE_MIN_GB)
      .bindNamedParameter(JvmHeapSizeMaxGB.class, JVM_HEAP_SIZE_MAX_GB)
      .bindNamedParameter(JvmOptions.class, JVM_OPTIONS)
      .bindNamedParameter(FlowControlWriteBufferHighMarkBytes.class,
          FLOW_CONTROL_WRITE_BUFFER_HIGH_MARK_BYTES)
      .bindNamedParameter(FlowControlWriteBufferLowMarkBytes.class,
          FLOW_CONTROL_WRITE_BUFFER_LOW_MARK_BYTES)
      .bindNamedParameter(OperatorInputBufferCapacity.class, OPERATOR_INPUT_BUFFER_CAPACITY)
      .bindNamedParameter(OperatorInputBufferRecoverTrigger.class,
          OPERATOR_INPUT_BUFFER_RECOVER_TRIGGER)
      .bindNamedParameter(TcpConnectionTimeoutMillis.class, TCP_CONNECTION_TIMEOUT_MILLIS)
      .bindNamedParameter(TcpReceiveBufferSizeBytes.class, TCP_RECEIVE_BUFFER_SIZE_BYTES)
      .bindNamedParameter(TcpSendBufferSizeBytes.class, TCP_SEND_BUFFER_SIZE_BYTES)
      .bindNamedParameter(LocalFragmentWorkerThreads.class, LOCAL_FRAGMENT_WORKER_THREADS)
      .bindNamedParameter(MasterHost.class, MASTER_HOST)
      .bindNamedParameter(MasterRpcPort.class, MASTER_RPC_PORT).build();

  @NamedParameter
  public class InstanceName implements Name<String> {
  }
  @NamedParameter
  public class DefaultInstancePath implements Name<String> {
  }
  @NamedParameter(default_value = MyriaConstants.STORAGE_SYSTEM_POSTGRESQL)
  public class StorageDbms implements Name<String> {
  }
  @NamedParameter(default_value = MyriaConstants.STORAGE_DATABASE_NAME)
  public class DefaultStorageDbName implements Name<String> {
  }
  @NamedParameter(default_value = MyriaConstants.STORAGE_JDBC_USERNAME)
  public class DefaultStorageDbUser implements Name<String> {
  }
  @NamedParameter
  public class DefaultStorageDbPassword implements Name<String> {
  }
  @NamedParameter(default_value = MyriaConstants.STORAGE_POSTGRESQL_PORT + "")
  public class DefaultStorageDbPort implements Name<Integer> {
  }
  @NamedParameter(default_value = MyriaConstants.DEFAULT_MYRIA_API_PORT + "")
  public class RestApiPort implements Name<Integer> {
  }
  @NamedParameter(default_value = "")
  public class ApiAdminPassword implements Name<String> {
  }
  @NamedParameter(default_value = "false")
  public class UseSsl implements Name<Boolean> {
  }
  @NamedParameter
  public class SslKeystorePath implements Name<String> {
  }
  @NamedParameter
  public class SslKeystorePassword implements Name<String> {
  }
  @NamedParameter(default_value = "false")
  public class EnableDebug implements Name<Boolean> {
  }
  @NamedParameter
  public class WorkerConf implements Name<Set<String>> {
  }
  @NamedParameter
  public class NumberVCores implements Name<Integer> {
  }
  @NamedParameter
  public class MemoryQuotaGB implements Name<Integer> {
  }
  @NamedParameter(default_value = "1")
  public class JvmHeapSizeMinGB implements Name<Integer> {
  }
  @NamedParameter(default_value = "2")
  public class JvmHeapSizeMaxGB implements Name<Integer> {
  }
  @NamedParameter(default_value = "-XX:+UseParallelGC -XX:+UseParallelOldGC")
  public class JvmOptions implements Name<String> {
  }
  @NamedParameter(default_value = (5 * MyriaConstants.MB) + "")
  public class FlowControlWriteBufferHighMarkBytes implements Name<Integer> {
  }
  @NamedParameter(default_value = (512 * MyriaConstants.KB) + "")
  public class FlowControlWriteBufferLowMarkBytes implements Name<Integer> {
  }
  @NamedParameter(default_value = "100")
  public class OperatorInputBufferCapacity implements Name<Integer> {
  }
  @NamedParameter(default_value = "80")
  public class OperatorInputBufferRecoverTrigger implements Name<Integer> {
  }
  @NamedParameter(default_value = "3000")
  public class TcpConnectionTimeoutMillis implements Name<Integer> {
  }
  @NamedParameter(default_value = (2 * MyriaConstants.MB) + "")
  public class TcpReceiveBufferSizeBytes implements Name<Integer> {
  }
  @NamedParameter(default_value = (5 * MyriaConstants.MB) + "")
  public class TcpSendBufferSizeBytes implements Name<Integer> {
  }
  @NamedParameter(default_value = "4")
  public class LocalFragmentWorkerThreads implements Name<Integer> {
  }
  @NamedParameter
  public class MasterHost implements Name<String> {
  }
  @NamedParameter
  public class MasterRpcPort implements Name<Integer> {
  }
}