package church.lowlow.user_api.admin.basicInfo.controller;

import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.rest_api.basicInfo.db.BasicInfoValidation;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.user_api.admin.basicInfo.service.BasicInfoService;
import church.lowlow.user_api.fileProcess.service.CommonFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
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
    private CommonFileService fileService;


    @GetMapping
    public String getBasicInfo() {

        return "admin/basicInfo/basicInfoView1";

    }


    @GetMapping("/create/chapter1")
    public String getBasicInfoCreateChapter1(Model model) {
        model.addAttribute("basicInfo", new BasicInfoDto());
        return "admin/basicInfo/basicInfoView1";
    }


    @PostMapping("/create/chapter2")
    public String getBasicInfoCreateChapter2(@ModelAttribute("basicInfo") BasicInfoDto basicInfoDto, Errors errors) {

        validation.chapter1Validate(basicInfoDto, errors);
        if(errors.hasErrors())
            return "admin/basicInfo/basicInfoView1";
        return "admin/basicInfo/basicInfoView2";
    }


    @PostMapping("/create/chapter3")
    public String getBasicInfoCreateChapter3(@ModelAttribute("basicInfo") BasicInfoDto basicInfoDto, Errors errors) {

        validation.chapter2Validate(basicInfoDto, errors);
        if(errors.hasErrors())
            return "admin/basicInfo/basicInfoView2";
        return "admin/basicInfo/basicInfoView3";
    }


    @PostMapping("/create/chapter1/reload")
    public String getBasicInfoCreateChapter1(@ModelAttribute("basicInfo")BasicInfoDto basicInfoDto) {
        return "admin/basicInfo/basicInfoView1";
    }


    @PostMapping("/create/chapter2/reload")
    public String getBasicInfoCreateChapter2(@ModelAttribute("basicInfo")BasicInfoDto basicInfoDto) {
        return "admin/basicInfo/basicInfoView2";
    }


    @PostMapping("/create")
    public String createBasicInfo(MultipartHttpServletRequest mRequest,
                                  @ModelAttribute("basicInfo") BasicInfoDto basicInfoDto,
                                  SessionStatus sessionStatus, Model model) {

        // file upload
        Map<String, FileDto> fileDtoMap = fileService.fileUpload(mRequest, "basicInfo");
        if(fileDtoMap.size() != 0) {
            fileDtoMap.forEach((key, value) -> {
                basicInfoService.fileDtoSave(key, value, basicInfoDto);
            });
        }

        // writer setting
        basicInfoService.setWriter(basicInfoDto);

        // DB save
        BasicInfoDto basicInfo = basicInfoService.createBasicInfo(basicInfoDto);

        // return
        if(basicInfo == null) {
            model.addAttribute("code", "E2");
            return "admin/basicInfo/basicInfoAlert";
        }

        sessionStatus.setComplete();
        model.addAttribute("code", "Cs");
        return "admin/basicInfo/basicInfoAlert";
    }


    @PutMapping("/update")
    public String updateBasicInfo(MultipartHttpServletRequest mRequest,
                                  @ModelAttribute("basicInfo") BasicInfoDto basicInfoDto,
                                  SessionStatus sessionStatus, Model model) {


        // fileDto preDataSetting
        BasicInfoDto preData = basicInfoService.getBasicInfo();
        basicInfoService.fileDtoPreDataSetting(preData, basicInfoDto);


        // file upload
        Map<String, FileDto> fileDtoMap = fileService.fileUpload(mRequest, "basicInfo");
        if(fileDtoMap.size() != 0) {
            fileDtoMap.forEach((key, value) -> {
                basicInfoService.fileDtoSave(key, value, basicInfoDto);
            });
        }

        // writer setting
        basicInfoService.setWriter(basicInfoDto);

        // DB update
        BasicInfoDto basicInfo = basicInfoService.updateBasicInfo(basicInfoDto);

        // return
        if(basicInfo == null) {
            model.addAttribute("code", "E2");
            return "admin/basicInfo/basicInfoAlert";
        }

        sessionStatus.setComplete();

        model.addAttribute("code", "Us");
        return "admin/basicInfo/basicInfoAlert";
    }

}
