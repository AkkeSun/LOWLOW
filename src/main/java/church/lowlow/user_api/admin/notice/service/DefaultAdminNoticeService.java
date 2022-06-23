package church.lowlow.user_api.admin.notice.service;

import church.lowlow.rest_api.notice.db.Notice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class DefaultAdminNoticeService implements AdminNoticeService {

    @Value("${restApiBaseUrl}")
    private String REST_API_URL;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public Notice getNotice(Long id) {

        return restTemplate.exchange(REST_API_URL + "/notices/" + id, HttpMethod.GET, null, Notice.class).getBody();

    }

}
