package church.lowlow.user_api.admin.accounting.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.user_api.admin.accounting.service.AdminAccountingService;
import church.lowlow.user_api.admin.accounting.service.DefaultExecService;
import church.lowlow.user_api.admin.accounting.service.ExcelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/admin/accounting")
@Log4j2
public class AdminAccountingController {

    @Autowired
    private AdminAccountingService accountingService;

    @Value("${fileUploadPath}")
    public String fileUploadPath;

    // ========== List View ==========
    @GetMapping
    public String getAccountingListView() {
        return "admin/accounting/accountingList";
    }


    // ========== Create View ==========
    @GetMapping("/create")
    public String getAccountingCreateView(Model model) {
        model.addAttribute("accounting", new Accounting());
        return "admin/accounting/accountingCreate";
    }


    // ========== Detail (Update) View ==========
    @GetMapping("/{id}")
    public String getAccountingDetailView(@PathVariable Long id, Model model) {

        //Accounting accounting = accountingService.getAccounting(id);
        model.addAttribute("userId", id);

        return "admin/accounting/accountingDetail";

    }


    // =================== 엑셀파일 다운로드 =====================
    @GetMapping("/excelDown")
    public void download(SearchDto searchDto, HttpServletResponse response) throws IOException {
        ExcelService excelService = new DefaultExecService(fileUploadPath + "/excel/", accountingService);
        excelService.excelFileDownload(searchDto, response);
    }



}
