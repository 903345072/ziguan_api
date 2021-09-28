package annotation;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import validator.checkTradeTimeValidator;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER,ElementType.TYPE})
@Constraint(validatedBy =  checkTradeTimeValidator.class)
@Retention(RUNTIME)
public @interface checkTradeTime  {
    String message() default "不在交易时间范围内";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        checkTradeTime[] value();
    }
}
