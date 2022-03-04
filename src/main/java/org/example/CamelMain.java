package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.main.Main;
import org.apache.camel.support.jsse.*;

@Slf4j
public class CamelMain {
  public static void main(String[] args) {
    try {
      final String passphrase = "passphrase";

      final KeyStoreParameters clientKeyStoreParameters = new KeyStoreParameters();
      clientKeyStoreParameters.setResource("admin.jks");
      clientKeyStoreParameters.setPassword(passphrase);

      final KeyManagersParameters keyManagersParameters = new KeyManagersParameters();
      keyManagersParameters.setKeyStore(clientKeyStoreParameters);
      keyManagersParameters.setKeyPassword(passphrase);

      final KeyStoreParameters trustKeyStoreParameters = new KeyStoreParameters();
      trustKeyStoreParameters.setResource("truststore.jks");
      trustKeyStoreParameters.setPassword(passphrase);

      final TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
      trustManagersParameters.setKeyStore(trustKeyStoreParameters);

      final SSLContextParameters sslContextParameters = new SSLContextParameters();
      sslContextParameters.setKeyManagers(keyManagersParameters);
      sslContextParameters.setTrustManagers(trustManagersParameters);
      final Main main = new Main();
      main.bind("sslContextParameters", sslContextParameters);
      // https://camel.apache.org/components/latest/index.html
      // https://camel.apache.org/components/latest/stream-component.html
      // https://camel.apache.org/components/latest/kafka-component.html
      main.configure().addLambdaRouteBuilder(builder -> builder
              .from("ahc-wss://server.local:8080/ws?sslContextParameters=#sslContextParameters")
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
