package church.lowlow.user_api.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/members")
public class UserMemberController {

    // ========== List View ==========
    @GetMapping
    public String getMemberListView() {
        return "admin/member/memberList";
    }

    // ========== Create View ==========
    @GetMapping("/create")
    public String getMemberCreateView() {
        return "admin/member/memberCreate";
    }

}
