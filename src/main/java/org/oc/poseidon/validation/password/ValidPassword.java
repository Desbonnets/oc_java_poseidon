package org.oc.poseidon.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Le mot de passe doit contenir au moins 8 caractères, une majuscule, un chiffre et un symbole.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
