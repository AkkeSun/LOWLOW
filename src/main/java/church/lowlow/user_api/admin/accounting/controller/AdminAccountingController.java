package church.lowlow.user_api.admin.accounting.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.user_api.admin.accounting.util.ExcelUtil;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Controller
@RequestMapping("/admin/accounting")
@Log4j2
public class AdminAccountingController {


    @Autowired
    private ExcelUtil excelUtil;

    @Value("${fileUploadPath}")
    private String fileUploadPath;

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

        model.addAttribute("userId", id);

        return "admin/accounting/accountingDetail";
    }


    // =================== 엑셀파일 생성 =====================
    @ResponseBody
    @PostMapping("/excelDown")
    public HashMap<String, String> download(@RequestBody HashMap<String, Object> map) throws IOException {

        // 데이터 추출
        ArrayList<Map> accountingList = (ArrayList<Map>) map.get("accountingList");
        LinkedHashMap<String, Object> statisticsMap = (LinkedHashMap<String, Object>) map.get("statisticsMap");
        SearchDto searchDto = excelUtil.makeSearchDto(map);

        // 엑셀파일 생성
        File file = excelUtil.accountingExcelCreate(searchDto, accountingList, statisticsMap);

        // return
        HashMap<String, String> returnMap = new HashMap<>();
        returnMap.put("fileName", file.getName());

        return returnMap;
    }


    // =================== 엑셀파일 다운로드 =====================
    @GetMapping("/down/{filename}")
    public void fileDownloader2(@PathVariable String filename, HttpServletResponse response) throws IOException {

        File localFile = new File(fileUploadPath + "/excel/"+ filename);

        byte[] fileByte = FileUtils.readFileToByteArray(localFile);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(LocalDate.now() + ".xlsx", "UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
        response.getOutputStream().close();

        localFile.delete();
    }
    
}