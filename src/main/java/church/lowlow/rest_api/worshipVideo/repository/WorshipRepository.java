package church.lowlow.rest_api.worshipVideo.repository;

import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import church.lowlow.rest_api.worshipVideo.queryDsl.WorshipVideoDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorshipRepository extends JpaRepository<WorshipVideo, Integer>, WorshipVideoDsl {
}

