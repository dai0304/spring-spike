FROM classmethod/openjdk-with-git:8-jdk
EXPOSE 8080
WORKDIR /opt/app/
RUN apt-get update && apt-get install -y zip jq
ADD ${jarFilename} /opt/app/
RUN mkdir -p /var/eb_log

ENV JAVA_OPTS -Xms512m -Xmx512m

CMD java \
  -server \
  \$JAVA_OPTS \
  -XX:+UseCompressedOops \
  -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap \
  -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
  -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
  -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
  -verbose:gc -Xloggc:/var/eb_log/gc_%t_pid%p.log \
  -XX:+PrintGCDateStamps -XX:+PrintGCDetails \
  -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M \
  -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/eb_log/ \
  -XX:ErrorFile=/var/eb_log/hs_err_pid%p.log \
  -Dsun.net.inetaddr.ttl=60 \
  -Djava.awt.headless=true \
  -Dfile.encoding=UTF-8 \
  -Dserver.port=8080 \
  -jar ${jarFilename}
