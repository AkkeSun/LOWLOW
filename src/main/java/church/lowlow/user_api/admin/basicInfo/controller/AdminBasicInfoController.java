package church.lowlow.user_api.admin.basicInfo.controller;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import church.lowlow.rest_api.basicInfo.db.BasicInfoDto;
import church.lowlow.user_api.admin.basicInfo.service.BasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/basicInfo")
@SessionAttributes({"basicInfo"})
public class AdminBasicInfoController {

    @Autowired
    private BasicInfoService service;

    @GetMapping
    public String getBasicInfo(Model model, @RequestParam(required = false) boolean afterCreate) {

        if(!afterCreate) {
            BasicInfo basicInfo = service.getBasicInfo();

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
    public String getBasicInfoCreateChapter2(Model model, @ModelAttribute("basicInfo") @Valid BasicInfoDto basicInfoDto, BindingResult bindingResult) {

        if(bindingResult.getAllErrors().size() != 2)
            return "admin/basicInfo/basicInfoCreate1";

        return "admin/basicInfo/basicInfoCreate2";
    }

    @PostMapping("/create/chapter2/reload")
    public String getBasicInfoCreateChapter2(@ModelAttribute("basicInfo")BasicInfoDto basicInfoDto) {
        return "admin/basicInfo/basicInfoCreate2";
    }


    @PostMapping("/create/chapter3")
    public String getBasicInfoCreateChapter3(@ModelAttribute("basicInfo") BasicInfoDto basicInfoDto) {
        return "admin/basicInfo/basicInfoCreate3";
    }


    @PostMapping("/create")
    public String createBasicInfo(@ModelAttribute("basicInfo") BasicInfoDto basicInfoDto) {

        // 세션 삭제


        return "redirect:/admin/basicInfo?afterCreate=true";
    }



}
