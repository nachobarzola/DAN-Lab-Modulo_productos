FROM openjdk:11.0.7-slim
LABEL maintainer="nachobarzola97@gmail.com"
ARG JAR_FILE
ADD target/${JAR_FILE} dan-ms-productos-0.0.1-SNAPSHOT.jar
RUN echo ${JAR_FILE}
EXPOSE 9001
ENTRYPOINT ["java","-jar","/dan-ms-productos-0.0.1-SNAPSHOT.jar"]
