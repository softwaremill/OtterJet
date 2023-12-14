package org.jetstreamDrop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "read")
public record ReaderConfigurationProperties(String subject) {}
