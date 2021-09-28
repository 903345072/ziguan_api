package validator;

import annotation.checkMoney;



import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.text.ParseException;

public class checkMoneyValidator implements ConstraintValidator<checkMoney,BigDecimal> {


    @Override
    public void initialize(checkMoney constraintAnnotation) {

    }

    @Override
    public boolean isValid(BigDecimal s, ConstraintValidatorContext constraintValidatorContext) {

        return false;
    }


}
