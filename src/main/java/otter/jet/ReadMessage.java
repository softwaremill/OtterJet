package otter.jet;

import java.time.LocalDateTime;

public record ReadMessage(String subject, String name, String body, LocalDateTime timestamp) {}
