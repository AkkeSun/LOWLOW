package church.lowlow.user_api.admin.basicInfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/basicInfo")
@SessionAttributes({"basicInfo"})
public class AdminBasicInfoController {


    @GetMapping("/1")
    public String getBasicInfoView1() {
        return "admin/basicInfo/basicInfoView1";
    }

    @GetMapping("/2")
    public String getBasicInfoView2() {
        return "admin/basicInfo/basicInfoView2";
    }

    @GetMapping("/3")
    public String getBasicInfoView3() {
        return "admin/basicInfo/basicInfoView3";
    }

}
