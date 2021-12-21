package church.lowlow.user_api.admin;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin/accounting")
public class AdminAccountingController {

    @Autowired
    private WebClient webClient;

    // ========== List View ==========
    @GetMapping
    public String getAccountingListView() {
        return "admin/accounting/accountingList";
    }

    // ========== Create View ==========
    @GetMapping("/create")
    public String getAccountingCreateView(Model model) {
        model.addAttribute("accounting", new Accounting());
        return "admin/accounting/accountingCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getAccuntingCreateView(@PathVariable Long id, Model model) {

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
