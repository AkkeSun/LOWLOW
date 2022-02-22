package church.lowlow.user_api.admin.member.controller;

import church.lowlow.rest_api.member.db.Member;
import church.lowlow.user_api.admin.member.service.AdminMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    @Autowired
    private AdminMemberService service;

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

        Member member = service.getMember(id);
        model.addAttribute("member", member);
        return "admin/member/memberDetail";

    }

}
