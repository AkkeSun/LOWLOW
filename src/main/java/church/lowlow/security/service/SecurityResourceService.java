package church.lowlow.security.service;

import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.repository.ResourcesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * factory bean 생성을 위한 service
 */
@Service
public class SecurityResourceService {

    @Autowired
    private ResourcesRepo resourcesRepository;

    // ========================= URL Resource 최종 Return ==========================
    // LinkedHashMap ( path : roleList )
    @Transactional
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getUrlResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        List<Resources> resourceList = resourcesRepository.findAllUrlResources();

        resourceList.forEach(resource -> {
            List<ConfigAttribute> roleList = new ArrayList<>();
            roleList.add(new SecurityConfig(resource.getResourceRole().getRoleName()));
            result.put(new AntPathRequestMatcher(resource.getResourceName()), roleList);
        });

        return result;
    }

}