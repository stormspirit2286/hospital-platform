/opt/homebrew/opt/java/libexec/openjdk.jdk/Contents/Home/bin/java -XX:TieredStopAtLevel=1 -Dspring.output.ansi.enabled=always -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -Dmanagement.endpoints.jmx.exposure.include=* -javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=51216 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath /Users/nguyenvanduy/Desktop/hospital-platform/patient-service/target/classes:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-actuator/4.0.6/spring-boot-starter-actuator-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter/4.0.6/spring-boot-starter-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-logging/4.0.6/spring-boot-starter-logging-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/ch/qos/logback/logback-classic/1.5.32/logback-classic-1.5.32.jar:/Users/nguyenvanduy/.m2/repository/ch/qos/logback/logback-core/1.5.32/logback-core-1.5.32.jar:/Users/nguyenvanduy/.m2/repository/org/apache/logging/log4j/log4j-to-slf4j/2.25.4/log4j-to-slf4j-2.25.4.jar:/Users/nguyenvanduy/.m2/repository/org/apache/logging/log4j/log4j-api/2.25.4/log4j-api-2.25.4.jar:/Users/nguyenvanduy/.m2/repository/org/slf4j/jul-to-slf4j/2.0.17/jul-to-slf4j-2.0.17.jar:/Users/nguyenvanduy/.m2/repository/jakarta/annotation/jakarta.annotation-api/3.0.0/jakarta.annotation-api-3.0.0.jar:/Users/nguyenvanduy/.m2/repository/org/yaml/snakeyaml/2.5/snakeyaml-2.5.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-micrometer-metrics/4.0.6/spring-boot-starter-micrometer-metrics-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-micrometer-metrics/4.0.6/spring-boot-micrometer-metrics-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-micrometer-observation/4.0.6/spring-boot-micrometer-observation-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-actuator-autoconfigure/4.0.6/spring-boot-actuator-autoconfigure-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-actuator/4.0.6/spring-boot-actuator-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-health/4.0.6/spring-boot-health-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/io/micrometer/micrometer-observation/1.16.5/micrometer-observation-1.16.5.jar:/Users/nguyenvanduy/.m2/repository/org/jspecify/jspecify/1.0.0/jspecify-1.0.0.jar:/Users/nguyenvanduy/.m2/repository/io/micrometer/micrometer-commons/1.16.5/micrometer-commons-1.16.5.jar:/Users/nguyenvanduy/.m2/repository/io/micrometer/micrometer-jakarta9/1.16.5/micrometer-jakarta9-1.16.5.jar:/Users/nguyenvanduy/.m2/repository/io/micrometer/micrometer-core/1.16.5/micrometer-core-1.16.5.jar:/Users/nguyenvanduy/.m2/repository/org/hdrhistogram/HdrHistogram/2.2.2/HdrHistogram-2.2.2.jar:/Users/nguyenvanduy/.m2/repository/org/latencyutils/LatencyUtils/2.0.3/LatencyUtils-2.0.3.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-data-jpa/4.0.6/spring-boot-starter-data-jpa-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-jdbc/4.0.6/spring-boot-starter-jdbc-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/com/zaxxer/HikariCP/7.0.2/HikariCP-7.0.2.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-data-jpa/4.0.6/spring-boot-data-jpa-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-data-commons/4.0.6/spring-boot-data-commons-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-persistence/4.0.6/spring-boot-persistence-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/data/spring-data-commons/4.0.5/spring-data-commons-4.0.5.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-hibernate/4.0.6/spring-boot-hibernate-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-jpa/4.0.6/spring-boot-jpa-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/jakarta/persistence/jakarta.persistence-api/3.2.0/jakarta.persistence-api-3.2.0.jar:/Users/nguyenvanduy/.m2/repository/org/hibernate/orm/hibernate-core/7.2.12.Final/hibernate-core-7.2.12.Final.jar:/Users/nguyenvanduy/.m2/repository/jakarta/transaction/jakarta.transaction-api/2.0.1/jakarta.transaction-api-2.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/jboss/logging/jboss-logging/3.6.3.Final/jboss-logging-3.6.3.Final.jar:/Users/nguyenvanduy/.m2/repository/org/hibernate/models/hibernate-models/1.0.1/hibernate-models-1.0.1.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/classmate/1.7.3/classmate-1.7.3.jar:/Users/nguyenvanduy/.m2/repository/org/glassfish/jaxb/jaxb-runtime/4.0.6/jaxb-runtime-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/glassfish/jaxb/jaxb-core/4.0.6/jaxb-core-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/eclipse/angus/angus-activation/2.0.3/angus-activation-2.0.3.jar:/Users/nguyenvanduy/.m2/repository/org/glassfish/jaxb/txw2/4.0.6/txw2-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/com/sun/istack/istack-commons-runtime/4.1.2/istack-commons-runtime-4.1.2.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-orm/7.0.7/spring-orm-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/data/spring-data-jpa/4.0.5/spring-data-jpa-4.0.5.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-aop/7.0.7/spring-aop-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-tx/7.0.7/spring-tx-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-beans/7.0.7/spring-beans-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/antlr/antlr4-runtime/4.13.2/antlr4-runtime-4.13.2.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-aspects/7.0.7/spring-aspects-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/aspectj/aspectjweaver/1.9.25.1/aspectjweaver-1.9.25.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-jdbc/4.0.6/spring-boot-jdbc-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-sql/4.0.6/spring-boot-sql-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-transaction/4.0.6/spring-boot-transaction-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-jdbc/7.0.7/spring-jdbc-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-liquibase/4.0.6/spring-boot-starter-liquibase-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-liquibase/4.0.6/spring-boot-liquibase-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/liquibase/liquibase-core/5.0.2/liquibase-core-5.0.2.jar:/Users/nguyenvanduy/.m2/repository/org/apache/commons/commons-collections4/4.5.0/commons-collections4-4.5.0.jar:/Users/nguyenvanduy/.m2/repository/org/apache/commons/commons-text/1.15.0/commons-text-1.15.0.jar:/Users/nguyenvanduy/.m2/repository/com/opencsv/opencsv/5.12.0/opencsv-5.12.0.jar:/Users/nguyenvanduy/.m2/repository/org/apache/commons/commons-lang3/3.19.0/commons-lang3-3.19.0.jar:/Users/nguyenvanduy/.m2/repository/commons-io/commons-io/2.21.0/commons-io-2.21.0.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-webmvc/4.0.6/spring-boot-starter-webmvc-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-jackson/4.0.6/spring-boot-starter-jackson-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-jackson/4.0.6/spring-boot-jackson-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat/4.0.6/spring-boot-starter-tomcat-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-tomcat-runtime/4.0.6/spring-boot-starter-tomcat-runtime-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/apache/tomcat/embed/tomcat-embed-core/11.0.21/tomcat-embed-core-11.0.21.jar:/Users/nguyenvanduy/.m2/repository/org/apache/tomcat/embed/tomcat-embed-el/11.0.21/tomcat-embed-el-11.0.21.jar:/Users/nguyenvanduy/.m2/repository/org/apache/tomcat/embed/tomcat-embed-websocket/11.0.21/tomcat-embed-websocket-11.0.21.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-tomcat/4.0.6/spring-boot-tomcat-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-http-converter/4.0.6/spring-boot-http-converter-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-web/7.0.7/spring-web-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-webmvc/4.0.6/spring-boot-webmvc-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-servlet/4.0.6/spring-boot-servlet-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-webmvc/7.0.7/spring-webmvc-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-expression/7.0.7/spring-expression-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-starter-config/5.0.1/spring-cloud-starter-config-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-starter/5.0.1/spring-cloud-starter-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-context/5.0.1/spring-cloud-context-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-restclient/4.0.6/spring-boot-restclient-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-http-client/4.0.6/spring-boot-http-client-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/security/spring-security-crypto/7.0.5/spring-security-crypto-7.0.5.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-commons/5.0.1/spring-cloud-commons-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/bouncycastle/bcprov-jdk18on/1.81/bcprov-jdk18on-1.81.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-config-client/5.0.1/spring-cloud-config-client-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.21/jackson-annotations-2.21.jar:/Users/nguyenvanduy/.m2/repository/org/apache/httpcomponents/client5/httpclient5/5.5.2/httpclient5-5.5.2.jar:/Users/nguyenvanduy/.m2/repository/org/apache/httpcomponents/core5/httpcore5/5.3.6/httpcore5-5.3.6.jar:/Users/nguyenvanduy/.m2/repository/org/apache/httpcomponents/core5/httpcore5-h2/5.3.6/httpcore5-h2-5.3.6.jar:/Users/nguyenvanduy/.m2/repository/tools/jackson/core/jackson-databind/3.1.2/jackson-databind-3.1.2.jar:/Users/nguyenvanduy/.m2/repository/tools/jackson/core/jackson-core/3.1.2/jackson-core-3.1.2.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-starter-netflix-eureka-client/5.0.1/spring-cloud-starter-netflix-eureka-client-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-netflix-eureka-client/5.0.1/spring-cloud-netflix-eureka-client-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-jackson2/4.0.6/spring-boot-jackson2-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.21.2/jackson-datatype-jdk8-2.21.2.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.21.2/jackson-datatype-jsr310-2.21.2.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/jackson/module/jackson-module-parameter-names/2.21.2/jackson-module-parameter-names-2.21.2.jar:/Users/nguyenvanduy/.m2/repository/com/netflix/eureka/eureka-client/2.0.5/eureka-client-2.0.5.jar:/Users/nguyenvanduy/.m2/repository/com/thoughtworks/xstream/xstream/1.4.21/xstream-1.4.21.jar:/Users/nguyenvanduy/.m2/repository/io/github/x-stream/mxparser/1.2.2/mxparser-1.2.2.jar:/Users/nguyenvanduy/.m2/repository/xmlpull/xmlpull/1.1.3.1/xmlpull-1.1.3.1.jar:/Users/nguyenvanduy/.m2/repository/jakarta/ws/rs/jakarta.ws.rs-api/4.0.0/jakarta.ws.rs-api-4.0.0.jar:/Users/nguyenvanduy/.m2/repository/jakarta/inject/jakarta.inject-api/2.0.1/jakarta.inject-api-2.0.1.jar:/Users/nguyenvanduy/.m2/repository/com/netflix/spectator/spectator-api/1.7.3/spectator-api-1.7.3.jar:/Users/nguyenvanduy/.m2/repository/org/slf4j/slf4j-api/2.0.17/slf4j-api-2.0.17.jar:/Users/nguyenvanduy/.m2/repository/org/apache/httpcomponents/httpclient/4.5.3/httpclient-4.5.3.jar:/Users/nguyenvanduy/.m2/repository/org/apache/httpcomponents/httpcore/4.4.16/httpcore-4.4.16.jar:/Users/nguyenvanduy/.m2/repository/commons-codec/commons-codec/1.19.0/commons-codec-1.19.0.jar:/Users/nguyenvanduy/.m2/repository/commons-configuration/commons-configuration/1.10/commons-configuration-1.10.jar:/Users/nguyenvanduy/.m2/repository/commons-lang/commons-lang/2.6/commons-lang-2.6.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.21.2/jackson-core-2.21.2.jar:/Users/nguyenvanduy/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.21.2/jackson-databind-2.21.2.jar:/Users/nguyenvanduy/.m2/repository/com/netflix/netflix-commons/netflix-eventbus/0.3.0/netflix-eventbus-0.3.0.jar:/Users/nguyenvanduy/.m2/repository/com/netflix/netflix-commons/netflix-infix/0.3.0/netflix-infix-0.3.0.jar:/Users/nguyenvanduy/.m2/repository/commons-jxpath/commons-jxpath/1.3/commons-jxpath-1.3.jar:/Users/nguyenvanduy/.m2/repository/joda-time/joda-time/2.3/joda-time-2.3.jar:/Users/nguyenvanduy/.m2/repository/org/antlr/antlr-runtime/3.4/antlr-runtime-3.4.jar:/Users/nguyenvanduy/.m2/repository/org/antlr/stringtemplate/3.2.1/stringtemplate-3.2.1.jar:/Users/nguyenvanduy/.m2/repository/antlr/antlr/2.7.7/antlr-2.7.7.jar:/Users/nguyenvanduy/.m2/repository/com/google/code/gson/gson/2.13.2/gson-2.13.2.jar:/Users/nguyenvanduy/.m2/repository/com/google/errorprone/error_prone_annotations/2.41.0/error_prone_annotations-2.41.0.jar:/Users/nguyenvanduy/.m2/repository/com/netflix/servo/servo-core/0.5.3/servo-core-0.5.3.jar:/Users/nguyenvanduy/.m2/repository/org/apache/commons/commons-math/2.2/commons-math-2.2.jar:/Users/nguyenvanduy/.m2/repository/javax/annotation/javax.annotation-api/1.2/javax.annotation-api-1.2.jar:/Users/nguyenvanduy/.m2/repository/org/codehaus/jettison/jettison/1.5.4/jettison-1.5.4.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-starter-loadbalancer/5.0.1/spring-cloud-starter-loadbalancer-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/cloud/spring-cloud-loadbalancer/5.0.1/spring-cloud-loadbalancer-5.0.1.jar:/Users/nguyenvanduy/.m2/repository/io/projectreactor/reactor-core/3.8.5/reactor-core-3.8.5.jar:/Users/nguyenvanduy/.m2/repository/org/reactivestreams/reactive-streams/1.0.4/reactive-streams-1.0.4.jar:/Users/nguyenvanduy/.m2/repository/io/projectreactor/addons/reactor-extra/3.6.0/reactor-extra-3.6.0.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-starter-cache/4.0.6/spring-boot-starter-cache-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-cache/4.0.6/spring-boot-cache-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-context-support/7.0.7/spring-context-support-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/com/stoyanr/evictor/1.0.0/evictor-1.0.0.jar:/Users/nguyenvanduy/.m2/repository/org/mapstruct/mapstruct/1.6.3/mapstruct-1.6.3.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-devtools/4.0.6/spring-boot-devtools-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot/4.0.6/spring-boot-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-core/7.0.7/spring-core-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/commons-logging/commons-logging/1.3.6/commons-logging-1.3.6.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/spring-context/7.0.7/spring-context-7.0.7.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-autoconfigure/4.0.6/spring-boot-autoconfigure-4.0.6.jar:/Users/nguyenvanduy/.m2/repository/org/postgresql/postgresql/42.7.10/postgresql-42.7.10.jar:/Users/nguyenvanduy/.m2/repository/org/checkerframework/checker-qual/3.52.0/checker-qual-3.52.0.jar:/Users/nguyenvanduy/.m2/repository/org/projectlombok/lombok/1.18.46/lombok-1.18.46.jar:/Users/nguyenvanduy/.m2/repository/jakarta/xml/bind/jakarta.xml.bind-api/4.0.4/jakarta.xml.bind-api-4.0.4.jar:/Users/nguyenvanduy/.m2/repository/jakarta/activation/jakarta.activation-api/2.1.4/jakarta.activation-api-2.1.4.jar:/Users/nguyenvanduy/.m2/repository/net/bytebuddy/byte-buddy/1.17.8/byte-buddy-1.17.8.jar:/Users/nguyenvanduy/.m2/repository/org/springframework/boot/spring-boot-web-server/4.0.6/spring-boot-web-server-4.0.6.jar com.duy.hospital.patientservice.PatientServiceApplication

.   ____          _            __ _ _
/\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
\\/  ___)| |_)| | | | | || (_| |  ) ) ) )
'  |____| .__|_| |_|_| |_\__, | / / / /
=========|_|==============|___/=/_/_/_/

:: Spring Boot ::                (v4.0.6)

2026-05-27T11:06:46.720+07:00  INFO 9503 --- [patient-service] [  restartedMain] c.d.h.p.PatientServiceApplication        : Starting PatientServiceApplication using Java 25.0.2 with PID 9503 (/Users/nguyenvanduy/Desktop/hospital-platform/patient-service/target/classes started by nguyenvanduy in /Users/nguyenvanduy/Desktop/hospital-platform)
2026-05-27T11:06:46.721+07:00  INFO 9503 --- [patient-service] [  restartedMain] c.d.h.p.PatientServiceApplication        : No active profile set, falling back to 1 default profile: "default"
2026-05-27T11:06:46.742+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
2026-05-27T11:06:46.742+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Exception on Url - http://localhost:8888:org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8888/patient-service/default": Connection refused. Will be trying the next url if available
2026-05-27T11:06:46.742+07:00  WARN 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Could not locate PropertySource ([ConfigServerConfigDataResource@4c1113a5 uris = array<String>['http://localhost:8888'], optional = true, profiles = 'default']): I/O error on GET request for "http://localhost:8888/patient-service/default": Connection refused
2026-05-27T11:06:46.743+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
2026-05-27T11:06:46.743+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Exception on Url - http://localhost:8888:org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8888/patient-service/default": Connection refused. Will be trying the next url if available
2026-05-27T11:06:46.743+07:00  WARN 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Could not locate PropertySource ([ConfigServerConfigDataResource@66d33bbe uris = array<String>['http://localhost:8888'], optional = true, profiles = 'default']): I/O error on GET request for "http://localhost:8888/patient-service/default": Connection refused
2026-05-27T11:06:46.743+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Fetching config from server at : http://localhost:8888
2026-05-27T11:06:46.743+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Exception on Url - http://localhost:8888:org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8888/patient-service/default": Connection refused. Will be trying the next url if available
2026-05-27T11:06:46.743+07:00  WARN 9503 --- [patient-service] [  restartedMain] o.s.c.c.c.ConfigServerConfigDataLoader   : Could not locate PropertySource ([ConfigServerConfigDataResource@7674cd52 uris = array<String>['http://localhost:8888'], optional = true, profiles = 'default']): I/O error on GET request for "http://localhost:8888/patient-service/default": Connection refused
2026-05-27T11:06:46.744+07:00  INFO 9503 --- [patient-service] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2026-05-27T11:06:46.744+07:00  INFO 9503 --- [patient-service] [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2026-05-27T11:06:47.099+07:00  INFO 9503 --- [patient-service] [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2026-05-27T11:06:47.106+07:00  INFO 9503 --- [patient-service] [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 4 ms. Found 0 JPA repository interfaces.
2026-05-27T11:06:47.210+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.cloud.context.scope.GenericScope     : BeanFactory id=b6568230-c76b-3f5d-ab09-60fc9455cdf0
2026-05-27T11:06:47.432+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.boot.tomcat.TomcatWebServer          : Tomcat initialized with port 8082 (http)
2026-05-27T11:06:47.438+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2026-05-27T11:06:47.438+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/11.0.21]
2026-05-27T11:06:47.454+07:00  INFO 9503 --- [patient-service] [  restartedMain] b.w.c.s.WebApplicationContextInitializer : Root WebApplicationContext: initialization completed in 710 ms
2026-05-27T11:06:47.565+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2026-05-27T11:06:47.642+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@20530e84
2026-05-27T11:06:47.642+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2026-05-27T11:06:47.725+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.database                       : Set default schema name to public
2026-05-27T11:06:49.448+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Reading resource: db/changelog/changes/001-create-patient-tables.sql
2026-05-27T11:06:49.485+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Creating database changelog table with name: public.databasechangelog
2026-05-27T11:06:49.507+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Reading from public.databasechangelog
2026-05-27T11:06:49.514+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.snapshot                       : Creating snapshot
2026-05-27T11:06:49.543+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.lockservice                    : Successfully acquired change log lock
2026-05-27T11:06:49.543+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.command                        : Using deploymentId: 9854807559
2026-05-27T11:06:49.544+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Reading from public.databasechangelog
2026-05-27T11:06:49.553+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Running Changeset: db/changelog/changes/001-create-patient-tables.sql::001-create-patients::duy
2026-05-27T11:06:49.563+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Custom SQL executed
2026-05-27T11:06:49.565+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : ChangeSet db/changelog/changes/001-create-patient-tables.sql::001-create-patients::duy ran successfully in 12ms
2026-05-27T11:06:49.568+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Running Changeset: db/changelog/changes/001-create-patient-tables.sql::001-index-patients::duy
2026-05-27T11:06:49.573+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Custom SQL executed
2026-05-27T11:06:49.574+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : ChangeSet db/changelog/changes/001-create-patient-tables.sql::001-index-patients::duy ran successfully in 6ms
2026-05-27T11:06:49.576+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Running Changeset: db/changelog/changes/001-create-patient-tables.sql::001-create-emergency-contacts::duy
2026-05-27T11:06:49.579+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Custom SQL executed
2026-05-27T11:06:49.580+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : ChangeSet db/changelog/changes/001-create-patient-tables.sql::001-create-emergency-contacts::duy ran successfully in 4ms
2026-05-27T11:06:49.581+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Running Changeset: db/changelog/changes/001-create-patient-tables.sql::001-index-emergency-contacts::duy
2026-05-27T11:06:49.582+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Custom SQL executed
2026-05-27T11:06:49.582+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : ChangeSet db/changelog/changes/001-create-patient-tables.sql::001-index-emergency-contacts::duy ran successfully in 1ms
2026-05-27T11:06:49.583+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Running Changeset: db/changelog/changes/001-create-patient-tables.sql::001-create-patient-insurances::duy
2026-05-27T11:06:49.586+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Custom SQL executed
2026-05-27T11:06:49.586+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : ChangeSet db/changelog/changes/001-create-patient-tables.sql::001-create-patient-insurances::duy ran successfully in 2ms
2026-05-27T11:06:49.587+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Running Changeset: db/changelog/changes/001-create-patient-tables.sql::001-index-patient-insurances::duy
2026-05-27T11:06:49.589+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : Custom SQL executed
2026-05-27T11:06:49.589+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.changelog                      : ChangeSet db/changelog/changes/001-create-patient-tables.sql::001-index-patient-insurances::duy ran successfully in 2ms
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : UPDATE SUMMARY
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : Run:                          6
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : Previously run:               0
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : Filtered out:                 0
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : -------------------------------
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : Total change sets:            6
2026-05-27T11:06:49.592+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.util                           : Update summary generated
2026-05-27T11:06:49.593+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.command                        : Update command completed successfully.
2026-05-27T11:06:49.593+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.ui                             : Liquibase: Update has been successful. Rows affected: 0
2026-05-27T11:06:49.594+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.lockservice                    : Successfully released change log lock
2026-05-27T11:06:49.595+07:00  INFO 9503 --- [patient-service] [  restartedMain] liquibase.command                        : Command execution complete
2026-05-27T11:06:49.630+07:00  INFO 9503 --- [patient-service] [  restartedMain] org.hibernate.orm.jpa                    : HHH008540: Processing PersistenceUnitInfo [name: default]
2026-05-27T11:06:49.653+07:00  INFO 9503 --- [patient-service] [  restartedMain] org.hibernate.orm.core                   : HHH000001: Hibernate ORM core version 7.2.12.Final
2026-05-27T11:06:49.837+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2026-05-27T11:06:49.871+07:00  INFO 9503 --- [patient-service] [  restartedMain] org.hibernate.orm.connections.pooling    : HHH10001005: Database info:
Database JDBC URL [jdbc:postgresql://localhost:5432/hospital_patient]
Database driver: PostgreSQL JDBC Driver
Database dialect: PostgreSQLDialect
Database version: 16.11
Default catalog/schema: hospital_patient/public
Autocommit mode: undefined/unknown
Isolation level: READ_COMMITTED [default READ_COMMITTED]
JDBC fetch size: none
Pool: DataSourceConnectionProvider
Minimum pool size: undefined/unknown
Maximum pool size: undefined/unknown
2026-05-27T11:06:50.036+07:00  INFO 9503 --- [patient-service] [  restartedMain] org.hibernate.orm.core                   : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2026-05-27T11:06:50.039+07:00  INFO 9503 --- [patient-service] [  restartedMain] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2026-05-27T11:06:50.330+07:00  INFO 9503 --- [patient-service] [  restartedMain] DiscoveryClientOptionalArgsConfiguration : Eureka HTTP Client uses RestClient.
2026-05-27T11:06:50.342+07:00  WARN 9503 --- [patient-service] [  restartedMain] iguration$LoadBalancerCaffeineWarnLogger : Spring Cloud LoadBalancer is currently working with the default cache. While this cache implementation is useful for development and tests, it's recommended to use Caffeine cache in production.You can switch to using Caffeine cache, by adding it and org.springframework.cache.caffeine.CaffeineCacheManager to the classpath.
2026-05-27T11:06:50.345+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 2 endpoints beneath base path '/actuator'
2026-05-27T11:06:50.379+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.n.eureka.InstanceInfoFactory       : Setting initial instance status as: STARTING
2026-05-27T11:06:50.388+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Initializing Eureka in region us-east-1
2026-05-27T11:06:50.389+07:00  INFO 9503 --- [patient-service] [  restartedMain] c.n.d.s.r.aws.ConfigClusterResolver      : Resolving eureka endpoints via configuration
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Disable delta property : false
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Single vip registry refresh property : null
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Force full registry fetch : false
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Application is null : false
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Registered Applications size is zero : true
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Application version is -1: true
2026-05-27T11:06:50.392+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Getting all instance registry info from the eureka server
2026-05-27T11:06:50.461+07:00  INFO 9503 --- [patient-service] [  restartedMain] c.n.d.s.t.d.RedirectingEurekaHttpClient  : Request execution error. endpoint=DefaultEndpoint{ serviceUrl='http://localhost:8761/eureka/}, exception=I/O error on GET request for "http://localhost:8761/eureka/apps/": Connect to http://localhost:8761 failed: Connection refused stacktrace=org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8761/eureka/apps/": Connect to http://localhost:8761 failed: Connection refused
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.createResourceAccessException(DefaultRestClient.java:779)
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.exchangeInternal(DefaultRestClient.java:624)
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.exchange(DefaultRestClient.java:572)
at org.springframework.web.client.RestClient$RequestHeadersSpec.exchange(RestClient.java:747)
at org.springframework.web.client.DefaultRestClient$DefaultResponseSpec.executeAndExtract(DefaultRestClient.java:924)
at org.springframework.web.client.DefaultRestClient$DefaultResponseSpec.toEntityInternal(DefaultRestClient.java:870)
at org.springframework.web.client.DefaultRestClient$DefaultResponseSpec.toEntity(DefaultRestClient.java:859)
at org.springframework.cloud.netflix.eureka.http.RestClientEurekaHttpClient.getApplicationsInternal(RestClientEurekaHttpClient.java:201)
at org.springframework.cloud.netflix.eureka.http.RestClientEurekaHttpClient.getApplications(RestClientEurekaHttpClient.java:137)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137)
at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.executeOnNewServer(RedirectingEurekaHttpClient.java:121)
at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.execute(RedirectingEurekaHttpClient.java:80)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137)
at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:120)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137)
at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:76)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134)
at com.netflix.discovery.DiscoveryClient.getAndStoreFullRegistry(DiscoveryClient.java:1046)
at com.netflix.discovery.DiscoveryClient.fetchRegistry(DiscoveryClient.java:961)
at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:410)
at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:245)
at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:240)
at org.springframework.cloud.netflix.eureka.CloudEurekaClient.<init>(CloudEurekaClient.java:68)
at org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration$RefreshableEurekaClientConfiguration.eurekaClient(EurekaClientAutoConfiguration.java:324)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:155)
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiateWithFactoryMethod(SimpleInstantiationStrategy.java:72)
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:152)
at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653)
at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:645)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1362)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1194)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:565)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525)
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$1(AbstractBeanFactory.java:372)
at org.springframework.cloud.context.scope.GenericScope$BeanLifecycleWrapper.getBean(GenericScope.java:373)
at org.springframework.cloud.context.scope.GenericScope.get(GenericScope.java:177)
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:369)
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:196)
at org.springframework.aop.target.SimpleBeanTargetSource.getTarget(SimpleBeanTargetSource.java:35)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration.getTargetObject(EurekaRegistration.java:128)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration.getEurekaClient(EurekaRegistration.java:116)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.springframework.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:281)
at org.springframework.cloud.context.scope.GenericScope$LockedScopedProxyFactoryBean.invoke(GenericScope.java:480)
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:719)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration$$SpringCGLIB$$0.getEurekaClient(<generated>)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.maybeInitializeClient(EurekaServiceRegistry.java:83)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.register(EurekaServiceRegistry.java:66)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration.start(EurekaAutoServiceRegistration.java:89)
at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:420)
at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:409)
at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:613)
at java.base/java.lang.Iterable.forEach(Iterable.java:75)
at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:379)
at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:306)
at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:1013)
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:624)
at org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143)
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:756)
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:445)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:321)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1365)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
at com.duy.hospital.patientservice.PatientServiceApplication.main(PatientServiceApplication.java:10)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:52)
Caused by: org.apache.hc.client5.http.HttpHostConnectException: Connect to http://localhost:8761 failed: Connection refused
at java.base/sun.nio.ch.Net.pollConnect(Native Method)
at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:639)
at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:550)
at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:594)
at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:284)
at java.base/java.net.Socket.connect(Socket.java:659)
at org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:226)
at org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:497)
at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:164)
at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:174)
at org.apache.hc.client5.http.impl.classic.ConnectExec.execute(ConnectExec.java:144)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.ProtocolExec.execute(ProtocolExec.java:195)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.ContentCompressionExec.execute(ContentCompressionExec.java:168)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.HttpRequestRetryExec.execute(HttpRequestRetryExec.java:112)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.RedirectExec.execute(RedirectExec.java:110)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.InternalHttpClient.doExecute(InternalHttpClient.java:185)
at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:87)
at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:55)
at org.apache.hc.client5.http.classic.HttpClient.executeOpen(HttpClient.java:183)
at org.springframework.http.client.HttpComponentsClientHttpRequest.executeInternal(HttpComponentsClientHttpRequest.java:99)
at org.springframework.http.client.AbstractStreamingClientHttpRequest.executeInternal(AbstractStreamingClientHttpRequest.java:87)
at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:80)
at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeWithRequestAndBody(AbstractBufferingClientHttpRequest.java:89)
at org.springframework.http.client.InterceptingClientHttpRequest$EndOfChainRequestExecution.execute(InterceptingClientHttpRequest.java:102)
at org.springframework.cloud.netflix.eureka.http.RestClientTransportClientFactory.lambda$newClient$2(RestClientTransportClientFactory.java:98)
at org.springframework.http.client.ClientHttpRequestInterceptor.lambda$apply$0(ClientHttpRequestInterceptor.java:89)
at org.springframework.http.client.InterceptingClientHttpRequest.executeInternal(InterceptingClientHttpRequest.java:73)
at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:50)
at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:80)
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.exchangeInternal(DefaultRestClient.java:614)
... 71 more

2026-05-27T11:06:50.461+07:00  WARN 9503 --- [patient-service] [  restartedMain] c.n.d.s.t.d.RetryableEurekaHttpClient    : Request execution failed with message: I/O error on GET request for "http://localhost:8761/eureka/apps/": Connect to http://localhost:8761 failed: Connection refused
2026-05-27T11:06:50.461+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : DiscoveryClient_PATIENT-SERVICE/192.168.1.10:patient-service:8082 - was unable to refresh its cache! This periodic background refresh will be retried in 30 seconds. status = Cannot execute request on any known server stacktrace = com.netflix.discovery.shared.transport.TransportException: Cannot execute request on any known server
at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:112)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$6.execute(EurekaHttpClientDecorator.java:137)
at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:76)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.getApplications(EurekaHttpClientDecorator.java:134)
at com.netflix.discovery.DiscoveryClient.getAndStoreFullRegistry(DiscoveryClient.java:1046)
at com.netflix.discovery.DiscoveryClient.fetchRegistry(DiscoveryClient.java:961)
at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:410)
at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:245)
at com.netflix.discovery.DiscoveryClient.<init>(DiscoveryClient.java:240)
at org.springframework.cloud.netflix.eureka.CloudEurekaClient.<init>(CloudEurekaClient.java:68)
at org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration$RefreshableEurekaClientConfiguration.eurekaClient(EurekaClientAutoConfiguration.java:324)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:155)
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiateWithFactoryMethod(SimpleInstantiationStrategy.java:72)
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:152)
at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653)
at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:645)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1362)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1194)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:565)
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:525)
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$1(AbstractBeanFactory.java:372)
at org.springframework.cloud.context.scope.GenericScope$BeanLifecycleWrapper.getBean(GenericScope.java:373)
at org.springframework.cloud.context.scope.GenericScope.get(GenericScope.java:177)
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:369)
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:196)
at org.springframework.aop.target.SimpleBeanTargetSource.getTarget(SimpleBeanTargetSource.java:35)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration.getTargetObject(EurekaRegistration.java:128)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration.getEurekaClient(EurekaRegistration.java:116)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.springframework.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:281)
at org.springframework.cloud.context.scope.GenericScope$LockedScopedProxyFactoryBean.invoke(GenericScope.java:480)
at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:719)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration$$SpringCGLIB$$0.getEurekaClient(<generated>)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.maybeInitializeClient(EurekaServiceRegistry.java:83)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry.register(EurekaServiceRegistry.java:66)
at org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration.start(EurekaAutoServiceRegistration.java:89)
at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:420)
at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:409)
at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:613)
at java.base/java.lang.Iterable.forEach(Iterable.java:75)
at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:379)
at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:306)
at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:1013)
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:624)
at org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:143)
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:756)
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:445)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:321)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1365)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
at com.duy.hospital.patientservice.PatientServiceApplication.main(PatientServiceApplication.java:10)
at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
at java.base/java.lang.reflect.Method.invoke(Method.java:565)
at org.springframework.boot.devtools.restart.RestartLauncher.run(RestartLauncher.java:52)

2026-05-27T11:06:50.461+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Initial registry fetch from primary servers failed
2026-05-27T11:06:50.461+07:00  WARN 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Using default backup registry implementation which does not do anything.
2026-05-27T11:06:50.461+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Initial registry fetch from backup servers failed
2026-05-27T11:06:50.461+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Starting heartbeat executor: renew interval is: 30
2026-05-27T11:06:50.462+07:00  INFO 9503 --- [patient-service] [  restartedMain] c.n.discovery.InstanceInfoReplicator     : InstanceInfoReplicator onDemand update allowed rate per min is 4
2026-05-27T11:06:50.462+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Discovery Client initialized at timestamp 1779854810462 with initial instances count: 0
2026-05-27T11:06:50.464+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.c.n.e.s.EurekaServiceRegistry        : Registering application PATIENT-SERVICE with eureka with status UP
2026-05-27T11:06:50.464+07:00  INFO 9503 --- [patient-service] [  restartedMain] com.netflix.discovery.DiscoveryClient    : Saw local status change event StatusChangeEvent [timestamp=1779854810464, current=UP, previous=STARTING]
2026-05-27T11:06:50.465+07:00  INFO 9503 --- [patient-service] [foReplicator-%d] com.netflix.discovery.DiscoveryClient    : DiscoveryClient_PATIENT-SERVICE/192.168.1.10:patient-service:8082: registering service...
2026-05-27T11:06:50.470+07:00  INFO 9503 --- [patient-service] [  restartedMain] o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 8082 (http) with context path '/'
2026-05-27T11:06:50.470+07:00  INFO 9503 --- [patient-service] [  restartedMain] .s.c.n.e.s.EurekaAutoServiceRegistration : Updating port to 8082
2026-05-27T11:06:50.478+07:00  INFO 9503 --- [patient-service] [  restartedMain] c.d.h.p.PatientServiceApplication        : Started PatientServiceApplication in 4.036 seconds (process running for 4.592)
2026-05-27T11:06:50.494+07:00  INFO 9503 --- [patient-service] [foReplicator-%d] c.n.d.s.t.d.RedirectingEurekaHttpClient  : Request execution error. endpoint=DefaultEndpoint{ serviceUrl='http://localhost:8761/eureka/}, exception=I/O error on POST request for "http://localhost:8761/eureka/apps/PATIENT-SERVICE": Connect to http://localhost:8761 failed: Connection refused stacktrace=org.springframework.web.client.ResourceAccessException: I/O error on POST request for "http://localhost:8761/eureka/apps/PATIENT-SERVICE": Connect to http://localhost:8761 failed: Connection refused
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.createResourceAccessException(DefaultRestClient.java:779)
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.exchangeInternal(DefaultRestClient.java:624)
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.exchange(DefaultRestClient.java:572)
at org.springframework.web.client.RestClient$RequestHeadersSpec.exchange(RestClient.java:747)
at org.springframework.web.client.DefaultRestClient$DefaultResponseSpec.executeAndExtract(DefaultRestClient.java:924)
at org.springframework.web.client.DefaultRestClient$DefaultResponseSpec.toBodilessEntity(DefaultRestClient.java:888)
at org.springframework.cloud.netflix.eureka.http.RestClientEurekaHttpClient.register(RestClientEurekaHttpClient.java:64)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59)
at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.executeOnNewServer(RedirectingEurekaHttpClient.java:121)
at com.netflix.discovery.shared.transport.decorator.RedirectingEurekaHttpClient.execute(RedirectingEurekaHttpClient.java:80)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59)
at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:120)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59)
at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:76)
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56)
at com.netflix.discovery.DiscoveryClient.register(DiscoveryClient.java:828)
at com.netflix.discovery.InstanceInfoReplicator.run(InstanceInfoReplicator.java:125)
at com.netflix.discovery.InstanceInfoReplicator$2.run(InstanceInfoReplicator.java:105)
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:545)
at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328)
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:309)
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090)
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614)
at java.base/java.lang.Thread.run(Thread.java:1474)
Caused by: org.apache.hc.client5.http.HttpHostConnectException: Connect to http://localhost:8761 failed: Connection refused
at java.base/sun.nio.ch.Net.pollConnect(Native Method)
at java.base/sun.nio.ch.Net.pollConnectNow(Net.java:639)
at java.base/sun.nio.ch.NioSocketImpl.timedFinishConnect(NioSocketImpl.java:550)
at java.base/sun.nio.ch.NioSocketImpl.connect(NioSocketImpl.java:594)
at java.base/java.net.SocksSocketImpl.connect(SocksSocketImpl.java:284)
at java.base/java.net.Socket.connect(Socket.java:659)
at org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:226)
at org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:497)
at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:164)
at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:174)
at org.apache.hc.client5.http.impl.classic.ConnectExec.execute(ConnectExec.java:144)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.ProtocolExec.execute(ProtocolExec.java:195)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.ContentCompressionExec.execute(ContentCompressionExec.java:168)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.HttpRequestRetryExec.execute(HttpRequestRetryExec.java:112)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.RedirectExec.execute(RedirectExec.java:110)
at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
at org.apache.hc.client5.http.impl.classic.InternalHttpClient.doExecute(InternalHttpClient.java:185)
at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:87)
at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:55)
at org.apache.hc.client5.http.classic.HttpClient.executeOpen(HttpClient.java:183)
at org.springframework.http.client.HttpComponentsClientHttpRequest.executeInternal(HttpComponentsClientHttpRequest.java:99)
at org.springframework.http.client.AbstractStreamingClientHttpRequest.executeInternal(AbstractStreamingClientHttpRequest.java:87)
at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:80)
at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeWithRequestAndBody(AbstractBufferingClientHttpRequest.java:89)
at org.springframework.http.client.InterceptingClientHttpRequest$EndOfChainRequestExecution.execute(InterceptingClientHttpRequest.java:102)
at org.springframework.cloud.netflix.eureka.http.RestClientTransportClientFactory.lambda$newClient$2(RestClientTransportClientFactory.java:98)
at org.springframework.http.client.ClientHttpRequestInterceptor.lambda$apply$0(ClientHttpRequestInterceptor.java:89)
at org.springframework.http.client.InterceptingClientHttpRequest.executeInternal(InterceptingClientHttpRequest.java:73)
at org.springframework.http.client.AbstractBufferingClientHttpRequest.executeInternal(AbstractBufferingClientHttpRequest.java:50)
at org.springframework.http.client.AbstractClientHttpRequest.execute(AbstractClientHttpRequest.java:80)
at org.springframework.web.client.DefaultRestClient$DefaultRequestBodyUriSpec.exchangeInternal(DefaultRestClient.java:614)
... 24 more

2026-05-27T11:06:50.494+07:00  WARN 9503 --- [patient-service] [foReplicator-%d] c.n.d.s.t.d.RetryableEurekaHttpClient    : Request execution failed with message: I/O error on POST request for "http://localhost:8761/eureka/apps/PATIENT-SERVICE": Connect to http://localhost:8761 failed: Connection refused
2026-05-27T11:06:50.494+07:00  WARN 9503 --- [patient-service] [foReplicator-%d] com.netflix.discovery.DiscoveryClient    : DiscoveryClient_PATIENT-SERVICE/192.168.1.10:patient-service:8082 - registration failed Cannot execute request on any known server

com.netflix.discovery.shared.transport.TransportException: Cannot execute request on any known server
at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:112) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:76) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.DiscoveryClient.register(DiscoveryClient.java:828) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.InstanceInfoReplicator.run(InstanceInfoReplicator.java:125) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.InstanceInfoReplicator$2.run(InstanceInfoReplicator.java:105) ~[eureka-client-2.0.5.jar:2.0.5]
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:545) ~[na:na]
at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328) ~[na:na]
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:309) ~[na:na]
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090) ~[na:na]
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614) ~[na:na]
at java.base/java.lang.Thread.run(Thread.java:1474) ~[na:na]

2026-05-27T11:06:50.496+07:00  WARN 9503 --- [patient-service] [foReplicator-%d] c.n.discovery.InstanceInfoReplicator     : There was a problem with the instance info replicator

com.netflix.discovery.shared.transport.TransportException: Cannot execute request on any known server
at com.netflix.discovery.shared.transport.decorator.RetryableEurekaHttpClient.execute(RetryableEurekaHttpClient.java:112) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator$1.execute(EurekaHttpClientDecorator.java:59) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.SessionedEurekaHttpClient.execute(SessionedEurekaHttpClient.java:76) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.shared.transport.decorator.EurekaHttpClientDecorator.register(EurekaHttpClientDecorator.java:56) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.DiscoveryClient.register(DiscoveryClient.java:828) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.InstanceInfoReplicator.run(InstanceInfoReplicator.java:125) ~[eureka-client-2.0.5.jar:2.0.5]
at com.netflix.discovery.InstanceInfoReplicator$2.run(InstanceInfoReplicator.java:105) ~[eureka-client-2.0.5.jar:2.0.5]
at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:545) ~[na:na]
at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:328) ~[na:na]
at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:309) ~[na:na]
at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1090) ~[na:na]
at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:614) ~[na:na]
at java.base/java.lang.Thread.run(Thread.java:1474) ~[na:na]

2026-05-27T11:06:50.951+07:00  INFO 9503 --- [patient-service] [on(3)-127.0.0.1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2026-05-27T11:06:50.951+07:00  INFO 9503 --- [patient-service] [on(3)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2026-05-27T11:06:50.952+07:00  INFO 9503 --- [patient-service] [on(3)-127.0.0.1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
