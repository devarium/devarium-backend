package io.devarium.api.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;

public class EmptyCollectionValidator implements
    ConstraintValidator<EmptyCollection, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty();
    }
}
