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
    public Map<String, MultipartFile> makeMultipartFileMap(MultipartHttpServletRequest mRequest) {

        Map<String, MultipartFile> fileMap = new LinkedHashMap<>();

        fileMap.put("image1", mRequest.getFile("image1"));
        fileMap.put("image2", mRequest.getFile("image2"));
        fileMap.put("image3", mRequest.getFile("image3"));
        fileMap.put("image4", mRequest.getFile("image4"));

        return fileMap;
    }

    @Override
    public Weekly getWeekly(Long id) {
        Weekly weekly = webClient.get()
                .uri("/weekly/{id}", id)
                .retrieve().bodyToMono(Weekly.class)
                .block();
        return weekly;
    }
}
