package otter.jet.store;

import otter.jet.reader.ReadMessage;

import java.util.function.Predicate;

public record Filters(String subject, String type, String bodyContent) {

    public static Filters of(String subject, String type, String bodyContent) {
        return new Filters(subject, type, bodyContent);
    }

    public static Filters of(String subject, String type) {
        return new Filters(subject, type, "");
    }


    public static Filters of(String subject) {
        return new Filters(subject, "" ,"");
    }


    public static Filters empty() {
        return new Filters("", "", "");
    }

    Predicate<ReadMessage> toPredicate() {
        return m -> (subject.isBlank() || m.subject().contains(subject)) &&
                (type.isBlank() || m.name().contains(type)) &&
                (bodyContent.isBlank() || m.body().contains(bodyContent));
    }
}
