package vn.com.tma.emsbackend.validation;

import vn.com.tma.emsbackend.validation.validator.IpAddressValidator;
import vn.com.tma.emsbackend.validation.validator.PortValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = PortValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Port {
    String message() default "";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
