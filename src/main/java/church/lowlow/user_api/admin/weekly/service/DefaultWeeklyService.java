package church.lowlow.user_api.admin.weekly.service;

import church.lowlow.rest_api.weekly.db.Weekly;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class DefaultWeeklyService implements WeeklyService{

    @Value("${restApiBaseUrl}")
    private String REST_API_URL;

    private RestTemplate restTemplate = new RestTemplate();


    @Override
    @Transactional
    public Weekly getWeekly(Long id) {

        return restTemplate.exchange(REST_API_URL + "/weekly/" + id, HttpMethod.GET, null, Weekly.class).getBody();
    }
}
