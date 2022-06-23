package church.lowlow.user_api.admin.gallery.service;

import church.lowlow.rest_api.gallery.db.Gallery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class DefaultAdminGalleryService implements AdminGalleryService{

    @Value("${restApiBaseUrl}")
    private String REST_API_URL;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional
    public Gallery getGallery(Long id) {

        return restTemplate.exchange(REST_API_URL + "/galleries/" + id, HttpMethod.GET, null, Gallery.class).getBody();
    }

}
