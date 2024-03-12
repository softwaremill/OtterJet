package otterjet.monitoring;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AccountDetailsResponse(
    String name, @JsonProperty("stream_detail") List<StreamDetailsResponse> streamDetails) {}
