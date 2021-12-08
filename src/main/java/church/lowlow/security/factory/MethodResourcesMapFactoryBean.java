package church.lowlow.security.factory;

import church.lowlow.security.service.SecurityResourceService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import java.util.LinkedHashMap;
import java.util.List;

public class MethodResourcesMapFactoryBean implements FactoryBean<LinkedHashMap<String, List<ConfigAttribute>>> {

    private SecurityResourceService resourceService;
    private LinkedHashMap<String, List<ConfigAttribute>> resourcesMap;

    public MethodResourcesMapFactoryBean(SecurityResourceService resourceService){
        this.resourceService = resourceService;
    }

    @Override
    public LinkedHashMap<String, List<ConfigAttribute>> getObject() {
        if (resourcesMap == null)
            resourcesMap = resourceService.getMethodResourceList();
        return resourcesMap;
    }

    @Override
    public Class<LinkedHashMap> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}