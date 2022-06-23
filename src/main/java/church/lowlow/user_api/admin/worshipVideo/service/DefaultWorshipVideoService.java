package church.lowlow.user_api.admin.worshipVideo.service;

import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import church.lowlow.user_api.admin.accounting.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class DefaultWorshipVideoService extends ExcelUtil implements WorshipVideoService {

    @Value("${restApiBaseUrl}")
    private String REST_API_URL;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public WorshipVideo getWorshipVideo(Long id) {

        return restTemplate.exchange(REST_API_URL + "/worshipVideos/" + id, HttpMethod.GET, null, WorshipVideo.class).getBody();
    }
}
