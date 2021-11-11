package church.lowlow.rest_api.gallary.repository;

import church.lowlow.rest_api.gallary.db.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
}
