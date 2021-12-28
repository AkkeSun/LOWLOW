package church.lowlow.user_api.admin;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.gallery.db.Gallery;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin/gallery")
@Log4j2
public class AdminGalleryController {

    @Autowired
    private WebClient webClient;

    // ========== List View ==========
    @GetMapping
    public String getAccountingListView() {
        return "admin/gallery/galleryList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getAccountingCreateView(Model model) {
        model.addAttribute("gallery", new Gallery());
        return "admin/gallery/galleryCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getAccountingDetailView(@PathVariable Long id, Model model) {

        Mono<Accounting> accountingMono = webClient
                .get()
                .uri("/accounting/{id}", id)
                .retrieve()
                .bodyToMono(Accounting.class);

        Accounting accounting = accountingMono.block();
        model.addAttribute("accounting", accounting);

        return "admin/accounting/accountingDetail";

    }


}
