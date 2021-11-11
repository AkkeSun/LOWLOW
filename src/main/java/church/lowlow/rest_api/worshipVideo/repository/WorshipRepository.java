package church.lowlow.rest_api.worshipVideo.repository;

import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorshipRepository extends JpaRepository<WorshipVideo, Integer> {
}
