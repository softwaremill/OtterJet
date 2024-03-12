package otterjet.assertions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.BiPredicate;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.jetbrains.annotations.NotNull;

public class ComparisonConfiguration {

  @NotNull
  public static RecursiveComparisonConfiguration configureReadMessageComparisonWithJSONBody() {
    return ignoreTimestampField().withEqualsForFields(compareAsJsonField(), "body").build();
  }

  @NotNull
  public static RecursiveComparisonConfiguration configureReadMessageComparison() {
    return ignoreTimestampField().build();
  }

  private static RecursiveComparisonConfiguration.Builder ignoreTimestampField() {
    return RecursiveComparisonConfiguration.builder().withIgnoredFields("timestamp");
  }

  @NotNull
  private static BiPredicate<String, String> compareAsJsonField() {
    return (String first, String second) -> {
      ObjectMapper mapper = new ObjectMapper();
      try {
        JsonNode firstNode = mapper.readTree(first);
        JsonNode secondNode = mapper.readTree(second);
        return firstNode.equals(secondNode);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
