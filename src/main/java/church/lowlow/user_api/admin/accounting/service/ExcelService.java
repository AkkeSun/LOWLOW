package church.lowlow.user_api.admin.accounting.service;

import church.lowlow.rest_api.common.entity.SearchDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ExcelService {

    void excelFileDownload(SearchDto searchDto, HttpServletResponse response) throws IOException;

}
