
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = IATACodeValidatorAirline.class)
@Target({
	ElementType.TYPE
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAirline {

	String message() default "{acme.validation.airport.iata-code-pattern}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
