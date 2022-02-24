package church.lowlow.rest_api.common.aop;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
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
        System.out.println();
        log.info("================================ [REST API REQUEST START] ================================");
        log.info("[REQUEST] path : " +  pjp.getSignature().getDeclaringTypeName() + "." +  pjp.getSignature().getName());

        //------------------------------------
        Object retVal = pjp.proceed();
        //------------------------------------

        //---------타겟 메서드 실행 후-----------
        log.info("[RESPONSE] " + retVal);
        log.info("================================ [REST API RESPONSE END] ================================");
        return retVal;
    }
}
