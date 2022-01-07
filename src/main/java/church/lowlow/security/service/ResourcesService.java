package church.lowlow.security.service;
import church.lowlow.security.metadata.UrlMetadataSource;
import church.lowlow.security.domain.dto.ResourcesDto;
import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.repository.ResourcesRepo;
import church.lowlow.security.repository.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ResourcesService {

    @Autowired
    private ResourcesRepo resourcesRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
     private UrlMetadataSource urlMetadataSource;

    @Transactional
    public void createResource(ResourcesDto dto) {

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setResourceRole(roleRepo.findByRoleName(dto.getRole()));
        resourcesRepo.save(resources);
        // 실시간 업데이트
        urlMetadataSource.reload();
    }

    @Transactional
    public Resources getResource(Long id) {
        return resourcesRepo.findById(id).orElseThrow(RuntimeException::new);
    }

    @Transactional
    public Page<Resources> getResourceWithPage(int page) {
        Pageable pageable = PageRequest.of(page, 5);
        return resourcesRepo.getListForPage(pageable);
    }

    @Transactional
    public void updateResources(Long id, ResourcesDto dto) {

        Resources resources = modelMapper.map(dto, Resources.class);
        resources.setResourceRole(roleRepo.findByRoleName(dto.getRole()));
        resources.setId(id);

        resourcesRepo.save(resources);
        // 실시간 업데이트
        urlMetadataSource.reload();
    }

    @Transactional
    public void delete(Long id){
        Optional<Resources> optional = resourcesRepo.findById(id);
        Resources resources = optional.orElseThrow(NullPointerException::new);
        resources.setBlock(true);
        resourcesRepo.save(resources);
    };

}