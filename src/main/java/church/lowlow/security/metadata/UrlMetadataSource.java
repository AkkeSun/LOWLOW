package church.lowlow.security.metadata;

import church.lowlow.security.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * URL 방식의 자원 관리
 */
public class UrlMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SecurityResourceService securityResourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    public UrlMetadataSource(LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap){
        this.resourceMap = resourceMap;
    }


    @Override
    // ========== 사전에 저장된 security 자원 중 일치하는 자원(Key)이 있다면 ROLE(value)을 리턴 ===========
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        // 현재 사용자가 요청하는 자원 객채
        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        if(resourceMap != null){
            for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : resourceMap.entrySet()){

                RequestMatcher mather = entry.getKey();
                if(mather.matches(request)) return entry.getValue();

            }
        }
        return null;
    }


    // =============== 자원이 새로 생성될 때 실시간 데이터 업데이트 =================
    public void reload(){
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadMap = securityResourceService.getUrlResourceList();
        Iterator<Map.Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadMap.entrySet().iterator();

        // 이전 인가정보 지우기
        resourceMap.clear();

        while(iterator.hasNext()){
            Map.Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
            resourceMap.put(entry.getKey(), entry.getValue());
        }
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

}