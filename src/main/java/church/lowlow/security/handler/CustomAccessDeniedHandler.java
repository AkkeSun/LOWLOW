package church.lowlow.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

/**
 * 접근 권한이 없는 사용자가 접근 시 실행
 */
@Log4j2
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private String errorPage = "/denied";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
    //    setDeniedHandlerLogging(request);
        String deniedURL = errorPage + "?exception=" + "Access is Denied";
        response.sendRedirect(deniedURL);
    }


    public void setDeniedHandlerLogging (HttpServletRequest request){
        String requestURI = request.getRequestURI();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        log.info("[접근 권한 에러] username : " + username + " || requestURI : " + requestURI);
    }
}
