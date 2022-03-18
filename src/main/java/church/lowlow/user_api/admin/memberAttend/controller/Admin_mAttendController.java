package church.lowlow.user_api.admin.memberAttend.controller;

import church.lowlow.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import static church.lowlow.rest_api.common.util.StringUtil.objNullToStr;


@Controller
@SessionAttributes({"belong"})
@RequestMapping("/admin/memberAttend")
public class Admin_mAttendController {

    @Autowired
    private AccountService accountService;

    // ========== List View ==========
    @GetMapping
    public String getMemberListView(Model model) {
        String belong = objNullToStr(accountService.getLoginUserBelong());
        model.addAttribute("belong", belong);
        return "admin/memberAttend/mAttendList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getMemberCreateView(Model model, String belong) {
        return "admin/memberAttend/mAttendCreate";
    }


    // ========== Detail View ==========
    @GetMapping("/detail")
    public String getMemberDetailView(Model model) {
        return "admin/memberAttend/mAttendDetail";
    }

}
