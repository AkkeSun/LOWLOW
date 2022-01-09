package church.lowlow.security.factory;

import church.lowlow.security.service.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;
import java.util.List;

public class UrlResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private SecurityResourceService resourceService;
    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceMap;

    public UrlResourcesMapFactoryBean(SecurityResourceService securityResourceService){
        this.resourceService = securityResourceService;
    }

    private void init() {
        resourceMap = resourceService.getUrlResourceList();
    }

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() {
        if(resourceMap == null)
            init();
        return resourceMap;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}