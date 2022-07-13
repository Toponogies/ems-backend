package vn.com.tma.emsbackend.model.validation.validator;

import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.model.validation.Port;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PortValidator implements ConstraintValidator<Port, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return (value > 0 && value < Constant.MAX_PORT_NUMBER);
    }
}
