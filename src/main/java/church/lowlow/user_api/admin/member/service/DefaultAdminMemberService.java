package church.lowlow.user_api.admin.member.service;

import church.lowlow.rest_api.member.db.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DefaultAdminMemberService implements AdminMemberService{

    @Autowired
    private WebClient webClient;

    @Override
    public Member getMember(Long id) {

        Mono<Member> memberMono = webClient
                .get()
                .uri("/members/{id}", id)
                .retrieve()
                .bodyToMono(Member.class);

        return memberMono.block();
    }
}
