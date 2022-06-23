package church.lowlow.user_api.admin.member.service;

import church.lowlow.rest_api.member.db.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class DefaultAdminMemberService implements AdminMemberService{

    @Value("${restApiBaseUrl}")
    private String REST_API_URL;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public Member getMember(Long id) {

        return restTemplate.exchange(REST_API_URL + "/members/" + id, HttpMethod.GET, null, Member.class).getBody();

    }
}
