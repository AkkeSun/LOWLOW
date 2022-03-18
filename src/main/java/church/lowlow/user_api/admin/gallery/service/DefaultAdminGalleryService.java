package church.lowlow.user_api.admin.gallery.service;

import church.lowlow.rest_api.gallery.db.Gallery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
public class DefaultAdminGalleryService implements AdminGalleryService{

    @Autowired
    private WebClient webClient;

    @Override
    @Transactional
    public Gallery getGallery(Long id) {

        Mono<Gallery> galleryMono = webClient
                .get()
                .uri("/galleries/{id}", id)
                .retrieve()
                .bodyToMono(Gallery.class);

        Gallery gallery = galleryMono.block();

        return gallery;
    }

}
