package church.lowlow.user_api.admin.basicInfo.controller;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.basicInfo.db.BasicInfoValidation;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.user_api.admin.basicInfo.service.BasicInfoService;
import church.lowlow.user_api.admin.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin/basicInfo")
@SessionAttributes({"basicInfo"})
public class AdminBasicInfoController {

    @Autowired
    private BasicInfoService basicInfoService;

    @Autowired
    private BasicInfoValidation validation;

    @Autowired
    private FileService fileService;


    @GetMapping
    public String getBasicInfo(Model model, @RequestParam(required = false) boolean afterCreate) {

        if(!afterCreate) {
            BasicInfo basicInfo = basicInfoService.getBasicInfo();

            if(basicInfo == null)
                return "admin/basicInfo/basicInfoNotFound";

            model.addAttribute("basicInfo", basicInfo);
            return "admin/basicInfo/basicInfoDetail";
        }

        return "admin/basicInfo/basicInfoDetail";
    }


    @GetMapping("/create/chapter1")
    public String getBasicInfoCreateChapter1(Model model) {
        model.addAttribute("basicInfo", new BasicInfoDto());
        return "admin/basicInfo/basicInfoCreate1";
    }

    @PostMapping("/create/chapter1/reload")
    public String getBasicInfoCreateChapter1(@ModelAttribute("basicInfo")BasicInfoDto basicInfoDto) {
        return "admin/basicInfo/basicInfoCreate1";
    }

    @PostMapping("/create/chapter2")
    public String getBasicInfoCreateChapter2(@ModelAttribute("basicInfo") BasicInfoDto basicInfoDto, Errors errors) {

        validation.chapter1Validate(basicInfoDto, errors);
        if(errors.hasErrors())
            return "admin/basicInfo/basicInfoCreate1";
        return "admin/basicInfo/basicInfoCreate2";

    }

    @PostMapping("/create/chapter2/reload")
    public String getBasicInfoCreateChapter2(@ModelAttribute("basicInfo")BasicInfoDto basicInfoDto) {
        return "admin/basicInfo/basicInfoCreate2";
    }



    @PostMapping("/create/chapter3")
    public String getBasicInfoCreateChapter3(@ModelAttribute("basicInfo") BasicInfoDto basicInfoDto, Errors errors) {

        validation.chapter2Validate(basicInfoDto, errors);
        if(errors.hasErrors())
            return "admin/basicInfo/basicInfoCreate2";
        return "admin/basicInfo/basicInfoCreate3";
    }


    @PostMapping("/create")
    public String createBasicInfo(MultipartHttpServletRequest mRequest,
                                  @ModelAttribute("basicInfo") BasicInfoDto basicInfoDto) {

        // make FileMap
        Map<String, MultipartFile> fileMap = basicInfoService.makeMultipartFileMap(mRequest);

        // file upload
        fileMap.forEach( (key, multipartFile) -> {
            if(!multipartFile.getOriginalFilename().equals("")) {
                FileDto fileDto = fileService.fileUpload(multipartFile);
                basicInfoService.fileDtoSave(key, fileDto, basicInfoDto);
            }
        });


        // DB save
        boolean result = basicInfoService.createBasicInfo(basicInfoDto);



        // 세션 삭제

        return "redirect:/admin/basicInfo?afterCreate=true";
    }

}
