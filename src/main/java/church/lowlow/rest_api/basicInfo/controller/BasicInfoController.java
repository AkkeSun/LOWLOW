package church.lowlow.rest_api.basicInfo.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.basicInfo.db.BasicInfoValidation;
import church.lowlow.rest_api.basicInfo.repository.BasicInfoRepository;
import church.lowlow.rest_api.basicInfo.resource.BasicInfoErrorsResource;
import church.lowlow.rest_api.basicInfo.resource.BasicInfoResource;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.Writer;
import church.lowlow.rest_api.gallery.db.Gallery;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.nio.file.Watchable;
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

    @Autowired
    private BasicInfoValidation validation;

    /**
     * CREATE API
     */
    @PostMapping
    @ApiOperation(value = "교회 기본 정보 등록", notes = "교회 기본 정보를 등록합니다", response = BasicInfo.class)
    public ResponseEntity createInfo(@RequestBody BasicInfoDto dto, Errors errors){

        // request param Logging
        logComponent.basicInfoDtoLogging(dto);

        // check 1
        validation.chapter1Validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new BasicInfoErrorsResource(errors));

        // check 2
        validation.chapter2Validate(dto, errors);
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
    @ApiOperation(value = "교회 기본 정보", notes = "교회 기본 정보를 출력합니다", response = BasicInfo.class)
    public ResponseEntity getInfo(@PathVariable Integer id){

        Optional<BasicInfo> optional = repository.findById(id);
        BasicInfo basicInfo = optional.orElseThrow(ArithmeticException::new);

        BasicInfoResource resource = new BasicInfoResource(basicInfo);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/list")
    @ApiOperation(value = "교회 기본 정보 리스트", notes = "교회 기본 정보 리스트를 출력합니다", response = BasicInfo.class)
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
    @ApiOperation(value = "교회 기본 정보 수정", notes = "교회 기본 정보를 수정합니다", response = BasicInfo.class)
    public ResponseEntity updateInfo(@RequestBody BasicInfoDto dto, @PathVariable Integer id, Errors errors){

        // request param Logging
        logComponent.basicInfoDtoLogging(dto);

        // check
        Optional<BasicInfo> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        validation.chapter1Validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new BasicInfoErrorsResource(errors));
        validation.chapter2Validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new BasicInfoErrorsResource(errors));

        // update
        BasicInfo info = modelMapper.map(dto, BasicInfo.class);
        info.setId(id);
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
    @ApiOperation(value = "교회 기본 정보 삭제", notes = "교회 기본 정보를 삭제합니다", response = BasicInfo.class)
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