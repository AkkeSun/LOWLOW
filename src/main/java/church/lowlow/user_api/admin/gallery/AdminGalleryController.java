package church.lowlow.user_api.admin.gallery;

import church.lowlow.rest_api.gallery.db.Gallery;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/galleries")
@Log4j2
public class AdminGalleryController {


    // ========== List View ==========
    @GetMapping
    public String getGalleryListView() {
        return "admin/gallery/galleryList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getGalleryCreateView(Model model) {
        model.addAttribute("gallery", new Gallery());
        return "admin/gallery/galleryCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getGalleryDetailView(@PathVariable Long id, Model model) {

        model.addAttribute("id", id);

        return "admin/gallery/galleryDetail";
    }

}
