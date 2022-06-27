package church.lowlow.user_api.admin.calendar;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/calendars")
public class AdminCalendarController {

    @GetMapping
    public String goCalendar() {
        return "admin/calendar/calendar";
    }

}
