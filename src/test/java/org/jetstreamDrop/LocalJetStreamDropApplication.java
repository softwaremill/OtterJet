package org.jetstreamDrop;

class LocalJetStreamDropApplication {
  public static void main(String[] args) {
    JetStreamDropApplication.createApplicationBuilder()
        .profiles("local")
        .initializers(new JetStreamInitializer())
        .run(args);
  }
}
