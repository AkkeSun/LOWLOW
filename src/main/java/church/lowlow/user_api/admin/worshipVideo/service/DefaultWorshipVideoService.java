package church.lowlow.user_api.admin.worshipVideo.service;

import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import church.lowlow.user_api.admin.accounting.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DefaultWorshipVideoService extends ExcelUtil implements WorshipVideoService {

    @Autowired
    private WebClient webClient;

    @Override
    public WorshipVideo getWorshipVideo(Long id) {
        Mono<WorshipVideo> worshipVideoMono = webClient
                .get()
                .uri("/worshipVideos/{id}", id)
                .retrieve()
                .bodyToMono(WorshipVideo.class);
        return worshipVideoMono.block();
    }
}
