package church.lowlow.user_api.common;

import church.lowlow.rest_api.accounting.db.Accounting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelUtil {

    /**
     * HSSF - Excel 97(-2007) 파일 포맷을 사용할 때 사용 , ex) HSSFWorkbook, HSSFSheet
     * XSSF - Excel 2007 OOXML (.xlsx) 파일 포맷을 사용할 때 사용 , ex) XSSFWorkbook, XSSFSheet
     *
     * Workbook  : 하나의 엑셀 파일을 의미
     * Sheet     : 엑셀파일(= Workbook)의 시트를 의미
     * Row, Cell : Sheet 안에 있는 행과 열을 의미
     *
     * [ 개발순서 ]
     *  1. workbook 을 생성한다.
     *  2. workbook 내에 sheet를 생성한다.
     *  3. sheet 내에 row를 생성한다.
     *  4. 하나의 row에 여러개의 cell을 생성한다. (= 하나의 행에 여러 열을 생성한다)
     */

    public static String filePath = "C:/upload";
    public static String fileNm = "accounting.xlsx";

    public static void accountingExcelCreate(List<Accounting> accountingList){



        /**********************************
         *       Workbook & Sheet 생성
         **********************************/
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Accounting");



        /**********************************
         *          폰트 스타일 설정
         **********************************/
        XSSFFont mainFont = workbook.createFont();
        mainFont.setFontName(HSSFFont.FONT_ARIAL); // 폰트 스타일
        mainFont.setFontHeightInPoints((short)20); // 폰트 크기
        mainFont.setBold(true); // Bold 설정
        mainFont.setColor(new XSSFColor(Color.decode("#457ba2"))); // 폰트 색 지정



        /**********************************
         *          테이블 스타일 설정
         **********************************/
        // ========== 메인 타이틀용 ===========
        CellStyle mainTitleStyle = workbook.createCellStyle();
        mainTitleStyle.setFont(mainFont); // cellStyle에 font를 적용
        mainTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);        // 가로 정렬
        mainTitleStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);// 세로정렬

        // ========== 입력 데이터용 ===========
        CellStyle tableStyle = workbook.createCellStyle();
        tableStyle.setBorderTop((short) 1); // 테두리 위쪽
        tableStyle.setBorderBottom((short) 1); // 테두리 아래쪽
        tableStyle.setBorderLeft((short) 1); // 테두리 왼쪽
        tableStyle.setBorderRight((short) 1); // 테두리 오른쪽
        tableStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);        // 가로 정렬
        tableStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);// 세로정렬



        /**********************************
         *         행 & 열 & 행의 갯수
         **********************************/
        XSSFRow xssfRow = null;
        XSSFCell xssfCell = null;
        int rowNo = 0;


        /**********************************
         *         셀 너비 설정
         **********************************/
        for(int i =0; i<=7; i++)
            sheet.setColumnWidth(i, 5500);



        /**********************************
         *        메인 타이틀 만들기
         **********************************/
        // 타이틀을 넣기 위한 셀 병합 (첫행, 마지막행, 첫열, 마지막열)
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 6));

        // 타이틀 생성
        xssfRow = sheet.createRow(rowNo++);       // 행 객체 생성 (생성하면 한 줄 쓰는 것)
        xssfCell = xssfRow.createCell((short) 0); // 열 위치 지정
        xssfCell.setCellStyle(mainTitleStyle);    // 셀에 스타일 지정
        xssfCell.setCellValue("회계 목록");        // 데이터 입력
        
        sheet.createRow(rowNo++);
        xssfRow = sheet.createRow(rowNo++); // 빈행 추가



        /**********************************
         *       엑셀 데이터 입력하기
         **********************************/

        Map<int, Object[]> data = new TreeMap<>();
        int key = 1;
        // table head
        data.put(key++, new Object[]{"교구", "이름", "전화번호", "금액", "종류", "날짜", "비고"});

        // table data
        for(Accounting accounting : accountingList){


        }
        data.put("2", new Object[]{"1", "cookie", "010-1111-1111", "bvb"});
        data.put("3", new Object[]{"2", "sickBBang", "010-2222-2222", "asa"});
        data.put("4", new Object[]{"3", "workingAnt", "010-3333-3333", "asda"});
        data.put("5", new Object[]{"4", "wow", "010-4444-4444", "asdad"});

        Set<String> keyset = data.keySet();

        for (String key : keyset) {
            xssfRow = sheet.createRow(rowNo++);
            Object[] objArr = data.get(key);

            int cellnum = 0;
            for (Object obj : objArr) {
                xssfCell = xssfRow.createCell(cellnum++);
                xssfCell.setCellStyle(tableStyle);
                if (obj instanceof String)
                    xssfCell.setCellValue((String)obj);
                else if (obj instanceof Integer)
                    xssfCell.setCellValue((Integer)obj);
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File(filePath, fileNm));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
