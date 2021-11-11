package church.lowlow.rest_api.calendar.repository;

import church.lowlow.rest_api.calendar.db.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
}
