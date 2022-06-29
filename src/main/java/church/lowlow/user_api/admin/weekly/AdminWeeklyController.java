package church.lowlow.user_api.admin.weekly;

import church.lowlow.rest_api.weekly.db.Weekly;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/weekly")
public class AdminWeeklyController {


    // ========== List View ==========
    @GetMapping
    public String getWeeklyView() {
        return "admin/weekly/weeklyList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getWeeklyCreateView(Model model) {
        model.addAttribute("weekly", new Weekly());
        return "admin/weekly/weeklyCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getWeeklyDetailView(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "admin/weekly/weeklyDetail";
    }

}
