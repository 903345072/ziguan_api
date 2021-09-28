package validator;

import annotation.checkTradeTime;
import com.util.TimeValidate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;

public class checkTradeTimeValidator implements ConstraintValidator<checkTradeTime,String> {

    @Override
    public void initialize(checkTradeTime constraintAnnotation) {

    }

    @Override
    public boolean isValid(String o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return TimeValidate.compareIsOpening();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
