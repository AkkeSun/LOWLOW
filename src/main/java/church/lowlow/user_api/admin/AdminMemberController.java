package church.lowlow.user_api.admin;

import church.lowlow.rest_api.member.db.Member;
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
    private WebClient webClient;

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

        Mono<Member> memberMono = webClient
                .get()
                .uri("/members/{id}", id)
                .retrieve()
                .bodyToMono(Member.class);

        Member member = memberMono.block();
        model.addAttribute("member", member);
        return "admin/member/memberDetail";

    }

}
