package otter.jet.reader;

import java.util.Objects;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "read")
public class ReaderConfigurationProperties {
  private String subject = "*";

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReaderConfigurationProperties that = (ReaderConfigurationProperties) o;
    return Objects.equals(subject, that.subject);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subject);
  }
}
