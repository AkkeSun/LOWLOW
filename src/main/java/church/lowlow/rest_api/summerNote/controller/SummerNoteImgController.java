package church.lowlow.rest_api.summerNote.controller;

import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.summerNote.db.SummerNoteImg;
import church.lowlow.rest_api.summerNote.db.SummerNoteImgDto;
import church.lowlow.rest_api.summerNote.repository.SummerNoteImgRepository;
import church.lowlow.rest_api.summerNote.resource.SummernoteImgResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/summerNote")
public class SummerNoteImgController {

    @Autowired
    private SummerNoteImgRepository repository;

    @Autowired
    private LogComponent logComponent;

    /**************************
     *       CREATE API
     **************************/
    @PostMapping
    public ResponseEntity createSummernoteImg(@RequestBody SummerNoteImgDto dto){

        // request param logging
        logComponent.summernoteDtoLogging(dto);

        // save
        FileDto fileDto = FileDto.builder().uploadName(dto.getUploadName()).originalName(dto.getOriginalName()).build();
        SummerNoteImg summernote = SummerNoteImg.builder().writer(getWriter()).image(fileDto).bbsType(dto.getBbsType()).build();
        SummerNoteImg newData = repository.save(summernote);
        URI createdUri = linkTo(SummerNoteImgController.class).slash(newData.getId()).toUri();

        // return
        SummernoteImgResource resource = new SummernoteImgResource(newData);
        resource.add(linkTo(SummerNoteImgController.class).slash(newData.getId()).withRel("delete-summerNote"));

        return ResponseEntity.created(createdUri).body(resource);
    }


    /**************************
     *       READ API
     **************************/
    @GetMapping
    public ResponseEntity getSummernoteImg() {

        List<SummerNoteImg> list = repository.findAll();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("summernoteImgList", list);

        return ResponseEntity.ok().body(resultMap);
    }


    /**************************
     *       DELETE API
     **************************/
    @DeleteMapping("/{id}")
    public ResponseEntity deleteData(@PathVariable Integer id){
        Optional<SummerNoteImg> optional = repository.findById(id);
        SummerNoteImg summerNoteImg = optional.orElseThrow(IllegalStateException::new);

        // delete
        repository.deleteById(id);

        // return
        SummernoteImgResource resource = new SummernoteImgResource(summerNoteImg);
        return ResponseEntity.ok(resource);
    }

}
