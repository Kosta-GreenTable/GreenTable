FROM tomcat:10.0-jdk17

# 기본 JVM 설정
ENV JAVA_OPTS="-Djava.awt.headless=true -Duser.timezone=Asia/Seoul -Xmx512m -Xms256m"

# Tomcat 파일 업로드 제한 완전 해제: setenv.sh로 시스템 속성 지정
RUN echo '#!/bin/bash' > /usr/local/tomcat/bin/setenv.sh && \
    echo 'export CATALINA_OPTS="$CATALINA_OPTS -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.FILE_COUNT_MAX=-1 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.fileCountMax=-1 -Dorg.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload.FILE_COUNT_MAX=-1 -Dorg.apache.commons.fileupload.FileUploadBase.FILE_COUNT_MAX=-1 -Dorg.apache.commons.fileupload.disk.DiskFileItem.maxFileCountThreshold=-1 -Dorg.apache.tomcat.util.http.fileupload.impl.DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD=0 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.fileSizeMax=-1 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.sizeMax=-1 -Dorg.apache.commons.fileupload.FileUploadBase.fileSizeMax=-1 -Dorg.apache.commons.fileupload.FileUploadBase.sizeMax=-1 -Djakarta.servlet.multipart.maxFileSize=-1 -Djakarta.servlet.multipart.maxRequestSize=-1 -Djakarta.servlet.multipart.fileSizeThreshold=0 -Dorg.apache.tomcat.util.http.fileupload.impl.FileItemIteratorImpl.FILE_COUNT_MAX=-1 -Dorg.apache.tomcat.util.http.fileupload.impl.FileItemStreamImpl.FILE_COUNT_MAX=-1 -Dorg.apache.tomcat.util.http.fileupload.DISABLE_TOMCAT_MULTIPART=true"' >> /usr/local/tomcat/bin/setenv.sh && \
    chmod +x /usr/local/tomcat/bin/setenv.sh

# ✅ catalina.properties에 직접 제한 해제 설정 추가
RUN echo "org.apache.tomcat.util.http.fileupload.FileUploadBase.fileCountMax=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.commons.fileupload.FileUploadBase.fileCountMax=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.commons.fileupload.disk.DiskFileItemFactory.sizeThreshold=0" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.tomcat.util.http.fileupload.FileUploadBase.fileSizeMax=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.tomcat.util.http.fileupload.FileUploadBase.sizeMax=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.commons.fileupload.FileUploadBase.fileSizeMax=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.commons.fileupload.FileUploadBase.sizeMax=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "jakarta.servlet.multipart.maxFileSize=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "jakarta.servlet.multipart.maxRequestSize=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "jakarta.servlet.multipart.fileSizeThreshold=0" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.tomcat.util.http.fileupload.impl.FileItemIteratorImpl.FILE_COUNT_MAX=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.tomcat.util.http.fileupload.impl.FileItemStreamImpl.FILE_COUNT_MAX=-1" >> /usr/local/tomcat/conf/catalina.properties && \
    echo "org.apache.tomcat.util.http.fileupload.DISABLE_TOMCAT_MULTIPART=true" >> /usr/local/tomcat/conf/catalina.properties

# ✅ JVM 시스템 프로퍼티로 직접 설정 (환경변수)
ENV CATALINA_OPTS="-Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.FILE_COUNT_MAX=-1 -Dorg.apache.commons.fileupload.FileUploadBase.FILE_COUNT_MAX=-1 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.fileCountMax=-1 -Dorg.apache.commons.fileupload.FileUploadBase.fileCountMax=-1 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.fileSizeMax=-1 -Dorg.apache.tomcat.util.http.fileupload.FileUploadBase.sizeMax=-1 -Dorg.apache.commons.fileupload.FileUploadBase.fileSizeMax=-1 -Dorg.apache.commons.fileupload.FileUploadBase.sizeMax=-1 -Djakarta.servlet.multipart.maxFileSize=-1 -Djakarta.servlet.multipart.maxRequestSize=-1 -Djakarta.servlet.multipart.fileSizeThreshold=0 -Dorg.apache.tomcat.util.http.fileupload.DISABLE_TOMCAT_MULTIPART=true"

# 이메일 설정 환경변수 (필요 시 override)
ENV MAIL_USERNAME=""
ENV MAIL_PASSWORD=""
ENV MAIL_SMTP_HOST="smtp.gmail.com"
ENV MAIL_SMTP_PORT="587"

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 기본 webapps 정리
RUN rm -rf /usr/local/tomcat/webapps/*

# WAR 파일 복사
COPY GreenTable.war /usr/local/tomcat/webapps/GreenTable.war

# MySQL 드라이버 설치
RUN curl -L -o /usr/local/tomcat/lib/mysql-connector-j.jar \
    https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar

# JavaMail 라이브러리 추가
RUN curl -L -o /usr/local/tomcat/lib/javax.mail.jar \
    https://repo1.maven.org/maven2/com/sun/mail/javax.mail/1.6.2/javax.mail-1.6.2.jar && \
    curl -L -o /usr/local/tomcat/lib/activation.jar \
    https://repo1.maven.org/maven2/javax/activation/activation/1.1.1/activation-1.1.1.jar

# AWS SDK for Java 라이브러리 추가
RUN curl -L -o /usr/local/tomcat/lib/aws-java-sdk-s3.jar \
    https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-s3/1.12.470/aws-java-sdk-s3-1.12.470.jar && \
    curl -L -o /usr/local/tomcat/lib/aws-java-sdk-core.jar \
    https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-core/1.12.470/aws-java-sdk-core-1.12.470.jar && \
    curl -L -o /usr/local/tomcat/lib/aws-java-sdk-kms.jar \
    https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-kms/1.12.470/aws-java-sdk-kms-1.12.470.jar && \
    curl -L -o /usr/local/tomcat/lib/jmespath-java.jar \
    https://repo1.maven.org/maven2/com/amazonaws/jmespath-java/1.12.470/jmespath-java-1.12.470.jar && \
    curl -L -o /usr/local/tomcat/lib/jackson-core.jar \
    https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.14.2/jackson-core-2.14.2.jar && \
    curl -L -o /usr/local/tomcat/lib/jackson-databind.jar \
    https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.14.2/jackson-databind-2.14.2.jar && \
    curl -L -o /usr/local/tomcat/lib/jackson-annotations.jar \
    https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.14.2/jackson-annotations-2.14.2.jar && \
    curl -L -o /usr/local/tomcat/lib/httpclient.jar \
    https://repo1.maven.org/maven2/org/apache/httpcomponents/httpclient/4.5.14/httpclient-4.5.14.jar && \
    curl -L -o /usr/local/tomcat/lib/httpcore.jar \
    https://repo1.maven.org/maven2/org/apache/httpcomponents/httpcore/4.4.16/httpcore-4.4.16.jar && \
    curl -L -o /usr/local/tomcat/lib/commons-logging.jar \
    https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar && \
    curl -L -o /usr/local/tomcat/lib/commons-codec.jar \
    https://repo1.maven.org/maven2/commons-codec/commons-codec/1.15/commons-codec-1.15.jar && \
    curl -L -o /usr/local/tomcat/lib/joda-time.jar \
    https://repo1.maven.org/maven2/joda-time/joda-time/2.12.5/joda-time-2.12.5.jar

# Apache Commons FileUpload2 라이브러리 추가 (Jakarta EE 호환 - Fallback 처리용)
RUN curl -L -o /usr/local/tomcat/lib/commons-fileupload2-jakarta.jar \
    https://repo1.maven.org/maven2/org/apache/commons/commons-fileupload2-jakarta/2.0.0-M1/commons-fileupload2-jakarta-2.0.0-M1.jar && \
    curl -L -o /usr/local/tomcat/lib/commons-fileupload2-core.jar \
    https://repo1.maven.org/maven2/org/apache/commons/commons-fileupload2-core/2.0.0-M1/commons-fileupload2-core-2.0.0-M1.jar && \
    curl -L -o /usr/local/tomcat/lib/commons-io.jar \
    https://repo1.maven.org/maven2/commons-io/commons-io/2.13.0/commons-io-2.13.0.jar

# Apache Commons FileUpload 라이브러리 추가 (FileCountLimitExceededException 우회용)
RUN curl -L -o /usr/local/tomcat/lib/commons-fileupload.jar \
    https://repo1.maven.org/maven2/commons-fileupload/commons-fileupload/1.5/commons-fileupload-1.5.jar && \
    curl -L -o /usr/local/tomcat/lib/commons-io.jar \
    https://repo1.maven.org/maven2/commons-io/commons-io/2.13.0/commons-io-2.13.0.jar

# Tomcat의 기본 파일 업로드 처리를 완전히 우회하기 위한 추가 라이브러리
RUN curl -L -o /usr/local/tomcat/lib/commons-fileupload2-portlet.jar \
    https://repo1.maven.org/maven2/org/apache/commons/commons-fileupload2-portlet/2.0.0-M1/commons-fileupload2-portlet-2.0.0-M1.jar

# context.xml 설정
RUN mkdir -p /usr/local/tomcat/conf/Catalina/localhost && \
    echo '<?xml version="1.0" encoding="UTF-8"?>' > /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '<Context reloadable="false">' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <WatchedResource>WEB-INF/web.xml</WatchedResource>' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <Resource name="jdbc/greentable"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              auth="Container"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              type="javax.sql.DataSource"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              driverClassName="com.mysql.cj.jdbc.Driver"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              url="jdbc:mysql://greentable_mysql:3306/greentable?useSSL=false&amp;allowPublicKeyRetrieval=true&amp;serverTimezone=Asia/Seoul&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;autoReconnect=true&amp;failOverReadOnly=false&amp;maxReconnects=10"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              username="root"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              password="greentable123"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              maxTotal="20"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              maxIdle="10"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              minIdle="5"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              maxWaitMillis="10000"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              testOnBorrow="true"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              validationQuery="SELECT 1"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              removeAbandoned="true"' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '              removeAbandonedTimeout="60" />' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <Environment name="org.apache.tomcat.util.http.fileupload.FileUploadBase.FILE_COUNT_MAX" value="-1" type="java.lang.String" />' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <Environment name="org.apache.commons.fileupload.FileUploadBase.FILE_COUNT_MAX" value="-1" type="java.lang.String" />' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <Environment name="org.apache.commons.fileupload.disk.DiskFileItem.maxFileCountThreshold" value="-1" type="java.lang.String" />' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <Parameter name="maxPostSize" value="-1" override="false" />' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '    <Parameter name="maxSwallowSize" value="-1" override="false" />' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml && \
    echo '</Context>' >> /usr/local/tomcat/conf/Catalina/localhost/GreenTable.xml

# web.xml에 multipart 설정 추가
RUN mkdir -p /tmp/webxml && \
    unzip -q /usr/local/tomcat/webapps/GreenTable.war WEB-INF/web.xml -d /tmp/webxml/ 2>/dev/null || echo "web.xml not found, creating new one" && \
    if [ -f /tmp/webxml/WEB-INF/web.xml ]; then \
    cp /tmp/webxml/WEB-INF/web.xml /tmp/web.xml.backup; \
    fi && \
    cat > /tmp/multipart-config.xml << 'WEBXML'
<multipart-config>
<max-file-size>-1</max-file-size>
<max-request-size>-1</max-request-size>
<file-size-threshold>0</file-size-threshold>
</multipart-config>
WEBXML

# server.xml 파일 완전 교체 (파일 업로드 제한 해제)
RUN cp /usr/local/tomcat/conf/server.xml /usr/local/tomcat/conf/server.xml.backup && \
    cat > /usr/local/tomcat/conf/server.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<Server port="8005" shutdown="SHUTDOWN">
<Listener className="org.apache.catalina.startup.VersionLoggerListener" />
<Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
<Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener" />
<Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener" />
<Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener" />

<GlobalNamingResources>
<Resource name="UserDatabase" auth="Container"
type="org.apache.catalina.UserDatabase"
description="User database that can be updated and saved"
factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
pathname="conf/tomcat-users.xml" />
</GlobalNamingResources>

<Service name="Catalina">
<Connector port="8080" protocol="HTTP/1.1"
connectionTimeout="20000"
redirectPort="8443"
maxPostSize="-1"
maxSwallowSize="-1"
maxParameterCount="-1"
maxHttpHeaderSize="65536"
useBodyEncodingForURI="true"
URIEncoding="UTF-8" />

<Engine name="Catalina" defaultHost="localhost">
<Realm className="org.apache.catalina.realm.LockOutRealm">
<Realm className="org.apache.catalina.realm.UserDatabaseRealm"
resourceName="UserDatabase"/>
</Realm>

<Host name="localhost"  appBase="webapps"
unpackWARs="true" autoDeploy="true">
<Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs"
prefix="localhost_access_log" suffix=".txt"
pattern="%h %l %u %t &quot;%r&quot; %s %b" />
</Host>
</Engine>
</Service>
</Server>
EOF

# 포트 노출
EXPOSE 8080

# Tomcat 실행
CMD ["catalina.sh", "run"]
