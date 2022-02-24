package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.user_api.admin.accounting.util.ExcelUtil;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultExecService extends ExcelUtil implements ExcelService {

    private final String fileDownloadPath;
    private final AdminAccountingService accountingService;

    public DefaultExecService(String fileDownloadPath, AdminAccountingService accountingService) {
        this.fileDownloadPath = fileDownloadPath;
        this.accountingService = accountingService;
    }

    @Override
    public void excelFileDownload(SearchDto searchDto, HttpServletResponse response) throws IOException {

        // 엑셀에 들어갈 데이터 로드
        List<LinkedHashMap<String, Object>> accountingList = accountingService.getAccountingList(searchDto);
        Map<String, Object> statisticsMap = accountingService.getStatisticsMap(searchDto);

        // 엑셀파일 생성
        accountingExcelCreate(searchDto, accountingList, statisticsMap);

        // 다운로드
        String fileName = "accounting.xlsx";
        byte[] fileByte = FileUtils.readFileToByteArray(new File(fileDownloadPath + fileName));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(LocalDate.now() + ".xlsx", "UTF-8")+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
