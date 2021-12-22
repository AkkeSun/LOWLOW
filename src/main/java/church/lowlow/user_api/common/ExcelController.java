package church.lowlow.user_api.common;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelController {

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


    public static String filePath = "C:\\poi_temp";
    public static String fileNm = "poi_making_file_test.xlsx";

    public static void main(String[] args) {

        // 행의 갯수
        int rowNo = 0;

        // Workbook 생성
        XSSFWorkbook workbook = new XSSFWorkbook();

        // Sheet 생성
        XSSFSheet sheet = workbook.createSheet("my Sheet");

        // 행과 열 생성
        XSSFRow xssfRow = null;
        XSSFCell xssfCell = null;



        // ============ 폰트 스타일 만들기 ============
        XSSFFont font = workbook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL); // 폰트 스타일
        font.setFontHeightInPoints((short)20); // 폰트 크기
        font.setBold(true); // Bold 설정
        font.setColor(new XSSFColor(Color.decode("#457ba2"))); // 폰트 색 지정



        // ============ 테이블 스타일 만들기 ============
        // 메인 타이틀용
        CellStyle mainTitleStyle = workbook.createCellStyle();
        sheet.setColumnWidth(0, (sheet.getColumnWidth(0))+(short)2048); // 0번째 컬럼 넓이 조절
        mainTitleStyle.setFont(font); // cellStyle에 font를 적용
        mainTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 정렬

        // 테이블 타이틀용 (굵기)
        CellStyle tableTitleStyle = workbook.createCellStyle();
        tableTitleStyle.setBorderTop((short) 3); // 테두리 위쪽
        tableTitleStyle.setBorderBottom((short) 3); // 테두리 아래쪽
        tableTitleStyle.setBorderLeft((short) 3); // 테두리 왼쪽
        tableTitleStyle.setBorderRight((short) 3); // 테두리 오른쪽

        // 테이블 데이터용 (굵기)
        CellStyle tableDataStyle = workbook.createCellStyle();
        tableDataStyle.setBorderTop((short) 1);
        tableDataStyle.setBorderBottom((short) 1);
        tableDataStyle.setBorderLeft((short) 1);
        tableDataStyle.setBorderRight((short) 1);




        // ============ 메인 타이틀 만들기 ============
        // 타이틀을 넣기 위한 셀 병합 (첫행, 마지막행, 첫열, 마지막열)
        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 4));

        // 타이틀 생성
        xssfRow = sheet.createRow(rowNo++);       // 행 객체 생성 (생성하면 한 줄 쓰는 것)
        xssfCell = xssfRow.createCell((short) 0); // 열 위치 지정
        xssfCell.setCellStyle(mainTitleStyle);    // 셀에 스타일 지정
        xssfCell.setCellValue("타이틀 입니다");     // 데이터 입력
        
        sheet.createRow(rowNo++);
        xssfRow = sheet.createRow(rowNo++); // 빈행 추가



        // ============ 테이블 타이틀 입력하기 ============
        ArrayList<String> tableTitle = (ArrayList<String>) Arrays.asList("셀1", "셀2", "셀3", "셀4");

        xssfRow = sheet.createRow(rowNo++);
        for(int i=0; i<tableTitle.size(); i++){
            xssfCell = xssfRow.createCell((short) i);
            xssfCell.setCellStyle(tableTitleStyle);
            xssfCell.setCellValue(tableTitle.get(i));
        }




        // ============ 테이블 데이터 입력하기 ============
        Map<String, Object[]> data = new TreeMap<>();
        data.put("1", new Object[]{"ID", "NAME", "PHONE_NUMBER", "saaaa"});      // 테이블 타이틀
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
