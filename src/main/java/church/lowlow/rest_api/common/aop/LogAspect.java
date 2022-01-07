package church.lowlow.rest_api.common.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Log4j2
public class LogAspect {

    @Pointcut("execution(* church.lowlow.rest_api..*Controller.*(..))")
    public void point1(){};


    @Around("point1()")
    public Object logAspect(ProceedingJoinPoint pjp) throws Throwable{

        //---------타겟 메서드 실행 전----------
        String param = Arrays.toString(pjp.getArgs());

        log.info("REQUEST <======= " + pjp.getSignature().getDeclaringTypeName() + "/"
                + pjp.getSignature().getName()  + " : " + param );

        //------------------------------------
        Object retVal = pjp.proceed();
        //------------------------------------

        //---------타겟 메서드 실행 후-----------
        log.info("RESPONSE =======> " + pjp.getSignature().getDeclaringTypeName() + "/"
                + pjp.getSignature().getName() + " : " + retVal);

        return retVal;
    }
}
