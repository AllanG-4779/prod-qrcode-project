FROM amazoncorretto:17
COPY target/qrcodepayment-local.jar localdocker.jar
EXPOSE 8001
ENTRYPOINT ["java", "-jar", "localdocker.jar"]
