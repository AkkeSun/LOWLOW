package church.lowlow.security.service;
import church.lowlow.security.UrlMetadataSource;
import church.lowlow.security.domain.dto.ResourcesDto;
import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.repository.ResourcesReop;
import church.lowlow.security.repository.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ResourcesService {

    @Autowired
    private ResourcesReop resourcesRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
     private UrlMetadataSource urlMetadataSource;

    @Transactional
    public void createResource(ResourcesDto dto) {

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setResourceRole(roleRepo.findByRoleName(dto.getRoleName()));
        resourcesRepo.save(resources);
        // 실시간 업데이트
        urlMetadataSource.reload();
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesRepo.findAllResources();
    }

    @Transactional
    public Resources getResource(Long id) {
        Optional<Resources> optional = resourcesRepo.findById(id);
        return optional.orElseThrow(NullPointerException::new);
    }

    @Transactional
    public void updateResources(Long id, ResourcesDto dto) {

        Optional<Resources> optional = resourcesRepo.findById(id);
        optional.orElseThrow(NullPointerException::new);

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setResourceRole(roleRepo.findByRoleName(dto.getRoleName()));
        resources.setId(id);

        resourcesRepo.save(resources);
        // 실시간 업데이트
        urlMetadataSource.reload();
    }
}