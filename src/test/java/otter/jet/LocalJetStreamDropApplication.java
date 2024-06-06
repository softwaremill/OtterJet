package otter.jet;

class LocalOtterJetApplication {
  public static void main(String[] args) {
    OtterJetApplication.createApplicationBuilder()
        .profiles("local")
        .initializers(new JetStreamContainerInitializer())
        .run(args);
  }
}
