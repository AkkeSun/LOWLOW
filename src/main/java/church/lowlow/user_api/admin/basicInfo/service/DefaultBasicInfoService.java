package church.lowlow.user_api.admin.basicInfo.service;

import church.lowlow.rest_api.basicInfo.db.BasicInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class DefaultBasicInfoService implements BasicInfoService{


    @Autowired
    private WebClient webClient;
    @Override
    public BasicInfo getBasicInfo() {

        Mono<ResponseEntity<Object>> responseEntityMono = webClient.get()
                .uri("/basicInfo/list")
                .exchange()
                .flatMap(clientResponse -> clientResponse.toEntity(Object.class));

        ResponseEntity<Object> block = responseEntityMono.block();
        Map<String, Object> body = (Map<String, Object>) block.getBody();
        List <BasicInfo> basicInfoList = (List<BasicInfo>) body.get("basicInfoList");

        if (basicInfoList.size() == 0)
            return null;
        return basicInfoList.get(0);
    }



    @Override
    public BasicInfo createBasicInfo() {
        return null;
    }



    @Override
    public BasicInfo updateBasicInfo() {
        return null;
    }
}
