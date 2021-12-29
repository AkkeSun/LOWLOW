package church.lowlow.rest_api.gallery.repository;

import church.lowlow.rest_api.gallery.db.Gallery;
import church.lowlow.rest_api.gallery.queryDsl.GalleryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Integer>, GalleryDsl {
}
