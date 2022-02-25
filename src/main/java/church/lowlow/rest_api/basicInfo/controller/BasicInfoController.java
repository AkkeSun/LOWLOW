package church.lowlow.rest_api.basicInfo.controller;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.basicInfo.repository.BasicInfoRepository;
import church.lowlow.rest_api.basicInfo.resource.BasicInfoErrorsResource;
import church.lowlow.rest_api.basicInfo.resource.BasicInfoResource;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.gallery.db.Gallery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/basicInfo", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class BasicInfoController {

    @Autowired
    private BasicInfoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LogComponent logComponent;

    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createInfo(@RequestBody @Valid BasicInfoDto dto,
                                       Errors errors){

        // request param Logging
        logComponent.basicInfoDtoLogging(dto);

        // check
        if(errors.hasErrors())
            return badRequest().body(new BasicInfoErrorsResource(errors));

        // save
        BasicInfo info = modelMapper.map(dto, BasicInfo.class);
        info.setWriter(getWriter());
        BasicInfo newInfo = repository.save(info);
        URI createdUri = linkTo(BasicInfoController.class).slash(newInfo.getId()).toUri();

        // return
        BasicInfoResource resource = new BasicInfoResource(newInfo);
        resource.add(linkTo(BasicInfoController.class).slash(newInfo.getId()).withRel("update-basicInfo"));
        resource.add(linkTo(BasicInfoController.class).slash(newInfo.getId()).withRel("delete-basicInfo"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping("/{id}")
    public ResponseEntity getInfo(@PathVariable Integer id){

        Optional<BasicInfo> optional = repository.findById(id);
        BasicInfo basicInfo = optional.orElseThrow(ArithmeticException::new);

        BasicInfoResource resource = new BasicInfoResource(basicInfo);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/list")
    public ResponseEntity getBasicInfoList(){

        List<BasicInfo> list = repository.findAll();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("basicInfoList", list);

        return ResponseEntity.ok().body(resultMap);
    }

    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateInfo(@RequestBody @Valid BasicInfoDto dto,
                                        @PathVariable Integer id,
                                        Errors errors){

        // request param Logging
        logComponent.basicInfoDtoLogging(dto);

        // check
        Optional<BasicInfo> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new BasicInfoErrorsResource(errors));

        // save
        BasicInfo info = modelMapper.map(dto, BasicInfo.class);
        info.setId(id);
        info.setWriter(getWriter());
        BasicInfo updateInfo = repository.save(info);

        // return
        BasicInfoResource resource = new BasicInfoResource(updateInfo);
        resource.add(linkTo(BasicInfoController.class).slash(updateInfo.getId()).withRel("delete-basicInfo"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteInfo(@PathVariable Integer id, Resource resource){

        // check
        Optional<BasicInfo> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        repository.deleteById(id);

        // return
        resource.add(linkTo(BasicInfoController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }
}