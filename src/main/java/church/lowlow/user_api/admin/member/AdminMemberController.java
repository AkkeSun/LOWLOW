package church.lowlow.user_api.admin.member;

import church.lowlow.rest_api.member.db.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {


    // ========== List View ==========
    @GetMapping
    public String getMemberListView() {
        return "admin/member/memberList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getMemberCreateView(Model model) {
        model.addAttribute("member", new Member());
        return "admin/member/memberCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getMemberCreateView(@PathVariable Long id, Model model) {

        model.addAttribute("id", id);
        return "admin/member/memberDetail";

    }

}
