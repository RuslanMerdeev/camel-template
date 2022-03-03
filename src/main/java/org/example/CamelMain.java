package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.main.Main;

@Slf4j
public class CamelMain {
  public static void main(String[] args) {
    try {
      final Main main = new Main();
      // https://camel.apache.org/components/latest/index.html
      // https://camel.apache.org/components/latest/stream-component.html
      // https://camel.apache.org/components/latest/kafka-component.html
      main.configure().addLambdaRouteBuilder(builder -> builder
              .from("websocket://localhost:8081/ws?" +
                      "negotiationType=TLS&keyCertChainResource=file:client.crt&keyResource=file:client.key.converted&trustCertCollectionResource=file:ca.crt")
              .to("kafka://payments?brokers=localhost:9991,localhost:9992,localhost:9993")
              .to("stream://out")
              .log("response: ${body}")
      );
      main.run(args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
//  public static void main(String[] args) {
//    try {
//      final Main main = new Main();
//      main.configure()
//          .addLambdaRouteBuilder(builder -> builder
//              .from("stream://in")
//              .to("stream://out")
//              .log("body: ${body}")
//          );
//      main.run(args);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
}
