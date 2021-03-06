package church.lowlow.user_api.admin.worshipVideo;

import church.lowlow.rest_api.worshipVideo.db.WorshipVideo;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/worshipVideos")
@Log4j2
public class AdminWorshipVideoController {


    // ========== List View ==========
    @GetMapping
    public String getWorshipVideoListView() {
        return "admin/worshipVideo/worshipVideoList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getWorshipVideoCreateView(Model model) {
        model.addAttribute("worshipVideo", new WorshipVideo());
        return "admin/worshipVideo/worshipVideoCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getWorshipVideoDetailView(@PathVariable Long id, Model model) {

        model.addAttribute("id",id);
        return "admin/worshipVideo/worshipVideoDetail";

    }

}
