package church.lowlow.user_api.admin.weekly.service;

import church.lowlow.rest_api.weekly.db.Weekly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DefaultWeeklyService implements WeeklyService{

    @Autowired
    private WebClient webClient;

    @Override
    public Weekly getWeekly(Long id) {
        Weekly weekly = webClient.get()
                .uri("/weekly/{id}", id)
                .retrieve().bodyToMono(Weekly.class)
                .block();
        return weekly;
    }
}