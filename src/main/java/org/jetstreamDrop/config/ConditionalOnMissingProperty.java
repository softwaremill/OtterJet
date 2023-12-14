package org.jetstreamDrop.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ConditionalOnMissingProperty.OnMissingPropertyCondition.class)
public @interface ConditionalOnMissingProperty {

  String[] value();

  @Order(Ordered.HIGHEST_PRECEDENCE)
  class OnMissingPropertyCondition extends SpringBootCondition {

    @Override
    public ConditionOutcome getMatchOutcome(
        ConditionContext context, AnnotatedTypeMetadata metadata) {

      MultiValueMap<String, Object> annotationAttributes =
          metadata.getAllAnnotationAttributes(ConditionalOnMissingProperty.class.getName());
      for (Object values : annotationAttributes.get("value")) {
        for (String propertyName : (String[]) values) {
          if (context.getEnvironment().containsProperty(propertyName)) {
            // return NO match if there is a property of the given name:
            return ConditionOutcome.noMatch(ConditionMessage.of("Found property " + propertyName));
          }
        }
      }

      // return match if no matching property was found:
      return ConditionOutcome.match(ConditionMessage.of("None of the given properties found"));
    }
  }
}
