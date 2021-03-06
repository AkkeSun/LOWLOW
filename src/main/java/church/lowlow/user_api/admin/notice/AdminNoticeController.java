package church.lowlow.user_api.admin.notice;

import church.lowlow.rest_api.notice.db.Notice;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notices")
@Log4j2
public class AdminNoticeController {


    // ========== List View ==========
    @GetMapping
    public String getGalleryListView() {
        return "admin/notice/noticeList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getGalleryCreateView(Model model) {
        model.addAttribute("notice", new Notice());
        return "admin/notice/noticeCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getGalleryDetailView(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return "admin/notice/noticeDetail";

    }


}
