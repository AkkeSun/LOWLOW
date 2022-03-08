package church.lowlow.user_api.admin.weekly.controller;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.weekly.db.Weekly;
import church.lowlow.user_api.admin.file.service.FileService;
import church.lowlow.user_api.admin.weekly.service.WeeklyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/weekly")
public class AdminWeeklyController {

    @Autowired
    private WeeklyService weeklyService;

    @Autowired
    private FileService fileService;

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

        Weekly weekly = weeklyService.getWeekly(id);
        model.addAttribute("weekly", weekly);

        return "admin/weekly/weeklyDetail";

    }

}
