package church.lowlow.rest_api.common.util;

import church.lowlow.rest_api.common.entity.Writer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;


/**
 * 작성자에 대한 정보를 시큐리티에서 가져온다
 */
public class WriterUtil {

    public static Writer getWriter(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String remoteAddress = details.getRemoteAddress();

        Object principal = authentication.getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        return Writer.builder().writer(username).ip(remoteAddress).build();
    }
}
