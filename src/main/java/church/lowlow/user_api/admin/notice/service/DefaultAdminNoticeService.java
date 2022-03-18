package church.lowlow.user_api.admin.notice.service;

import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.notice.db.Notice;
import church.lowlow.user_api.admin.gallery.service.AdminGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
public class DefaultAdminNoticeService implements AdminNoticeService {

    @Autowired
    private WebClient webClient;

    @Override
    @Transactional
    public Notice getNotice(Long id) {

        Mono<Notice> noticeMono = webClient
                .get()
                .uri("/notices/{id}", id)
                .retrieve()
                .bodyToMono(Notice.class);

        Notice notice = noticeMono.block();

        return notice;
    }

}
