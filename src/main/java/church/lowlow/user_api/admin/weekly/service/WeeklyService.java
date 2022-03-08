package church.lowlow.user_api.admin.weekly.service;

import church.lowlow.rest_api.weekly.db.Weekly;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;

public interface WeeklyService {
    Weekly getWeekly(Long id);
}
