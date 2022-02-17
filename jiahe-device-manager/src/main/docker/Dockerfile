# From java image, version : 8
FROM java:8

# 挂载app目录
VOLUME /app

# COPY or ADD to image
COPY jiahe-device-manager-0.0.1-SNAPSHOT.jar app.jar

RUN bash -c "touch /app.jar"
EXPOSE 8890
ENTRYPOINT ["java", "-jar", "app.jar"]