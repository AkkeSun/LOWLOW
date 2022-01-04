package church.lowlow.user_api.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/calendar")
public class AdminCalendarController {



    @GetMapping
    public String getMemberListView() {
        return "admin/calendar/calendar";
    }


}
