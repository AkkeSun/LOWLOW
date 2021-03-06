package church.lowlow.user_api.admin.accounting.util;

import church.lowlow.rest_api.common.entity.SearchDto;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static church.lowlow.rest_api.common.util.DateUtil.localDateFormatter;
import static church.lowlow.rest_api.common.util.StringUtil.objNullToStr;

@Log4j2
@Component
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
     *  2. workbook 내에 sheet 를 생성한다.
     *  3. sheet 내에 row 를 생성한다.
     *  4. 하나의 row 에 여러개의 cell 을 생성한다. (= 하나의 행에 여러 열을 생성한다)
     */


    @Value("${fileUploadPath}")
    private String fileUploadPath;

    private String fileNm = UUID.randomUUID().toString().replaceAll("-", "") + ".xlsx";


    public File accountingExcelCreate(SearchDto searchDto, ArrayList<Map> accountingList, Map<String ,Object> statisticsMap){

        //============= Workbook & Sheet 생성 ===============
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet basicSheet = workbook.createSheet("전체 리스트");
        XSSFSheet statisticsSheet = workbook.createSheet("통계 분석");


        //============= 폰트스타일 설정 ===============
        XSSFFont mainFont = workbook.createFont();
        mainFont.setFontName(HSSFFont.FONT_ARIAL); // 폰트 스타일
        mainFont.setFontHeightInPoints((short)20); // 폰트 크기
        mainFont.setBold(true); // Bold 설정
        mainFont.setColor(new XSSFColor(Color.decode("#457ba2"))); // 폰트 색 지정


        //============= 테이블 스타일 설정 ===============
        CellStyle mainTitleStyle = workbook.createCellStyle();
        mainTitleStyle.setFont(mainFont); // cellStyle 에 font 를 적용
        mainTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);         // 가로 정렬
        mainTitleStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER); // 세로정렬

        CellStyle tableStyle = workbook.createCellStyle();
        tableStyle.setBorderTop((short) 1);
        tableStyle.setBorderBottom((short) 1);
        tableStyle.setBorderLeft((short) 1);
        tableStyle.setBorderRight((short) 1);
        tableStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        tableStyle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);


        // 엑셀 데이터 입력 생성
        accountingListExcelCreate(basicSheet, accountingList, mainTitleStyle, tableStyle, searchDto);
        accountingStatisticsExcelCreate(statisticsSheet, statisticsMap, mainTitleStyle, tableStyle);

        // 폴더 셋팅
        uploadFolderSetting();

        // 생성된 엑셀 파일로 추출
        File excelFile = new File( fileUploadPath + "/excel/" + fileNm);

        try {
            FileOutputStream out = new FileOutputStream(excelFile);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelFile;
    }



    protected void removeLocalFile(File targetFile) {

        try {
            Files.delete(Paths.get(targetFile.getPath()));
            log.info("[LOCAL FILE DELETE SUCCESS]");
        } catch (NoSuchFileException e) {
            System.out.println("[LOCAL FILE DELETE FAIL] File not Found");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("[LOCAL FILE DELETE FAIL] Directory is not Empty");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    /**************************************
     *      전체 리스트 엑셀 데이터 입력
     **************************************/
    public void accountingListExcelCreate(XSSFSheet xssfSheet, ArrayList<Map> accountingList,
                                                 CellStyle mainTitleStyle, CellStyle tableStyle,
                                                 SearchDto searchDto){


        //============= 행, 열, 인덱스변수 설정 ===============
        XSSFRow xssfRow = null;
        XSSFCell xssfCell = null;
        int rowNo = 0;
        int cellNo = 0;
        

        //============= 셀 너비 설정 ===============
        for(int i =0; i<=7; i++)
            xssfSheet.setColumnWidth(i, 5500);


        //============= 메인타이틀 만들기 ===============
        // 셀 병합 (첫행, 마지막행, 첫열, 마지막열)
        xssfSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 6));

        // 타이틀 생성
        xssfRow = xssfSheet.createRow(rowNo++);  // 행 객체 생성 (생성하면 한 줄 쓰는 것)
        xssfCell = xssfRow.createCell((short) 0); // 열 객채 생성 (param = 열의 위치)
        xssfCell.setCellStyle(mainTitleStyle);    // 셀에 스타일 지정
        xssfCell.setCellValue("전체 리스트" + searchDateConverter(searchDto));       // 데이터 입력

        // 빈행 추가
        xssfSheet.createRow(rowNo++);
        xssfRow = xssfSheet.createRow(rowNo++);



        //============= 엑셀 데이터 입력 ===============
        List<Object[]> excelDataList = new ArrayList<>();

        // table head
        excelDataList.add( new Object[]{"교구", "이름", "전화번호", "금액", "종류", "날짜", "비고"});

        // table data
        if(accountingList != null) {
            accountingList.forEach( map -> {

                String belong = "-";
                String phoneNum = "-";
                String offeringKind = offeringKindConverter((String)map.get("offeringKind"));
                String offeringDate = (String) map.get("offeringDate");
                String note = objNullToStr((String)map.get("note"), "-");
                Map<String, Object> member = (Map<String, Object>) map.get("member");
                String name = (String) member.get("name");
                int money = (int) map.get("money");

                if (!member.equals("익명")) {
                    belong = (String) member.get("belong");;
                    phoneNum = (String) member.get("phoneNumber");
                }
                excelDataList.add(new Object[]{belong, name, phoneNum, money, offeringKind, offeringDate, note});
            });

            // Data append
            for(Object[] excelData : excelDataList){

                xssfRow = xssfSheet.createRow(rowNo++); // 행 추가
                cellNo = 0; // 열 index 초기화

                for (Object item : excelData) {
                    xssfCell = xssfRow.createCell(cellNo++);
                    xssfCell.setCellStyle(tableStyle);

                    if (item instanceof String)
                        xssfCell.setCellValue((String) item);
                    else if (item instanceof Integer)
                        xssfCell.setCellValue((Integer) item);
                }
            }
        }
    }



    /**************************************
     *      헌금 통계분석 엑셀 데이터 입력
     **************************************/
    public void accountingStatisticsExcelCreate(XSSFSheet statisticsSheet, Map<String ,Object> statisticsMap,
                                                       CellStyle mainTitleStyle,  CellStyle tableStyle){

        //============= 행, 열, 인덱스 변수===============
        XSSFRow xssfRow = null;
        XSSFCell xssfCell = null;
        int rowNo = 0;
        int cellNo = 0;


        //============= 셀 너비설정 ===============
        for(int i = 0; i<=1; i++)
            statisticsSheet.setColumnWidth(i, 5500);


        //============= 메인 타이틀 만들기 (이르별 통계) ===============
        // 셀 병합 (첫행, 마지막행, 첫열, 마지막열)
        statisticsSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 1));

        // 타이틀 생성
        xssfRow = statisticsSheet.createRow(rowNo++);  // 행 객체 생성 (생성하면 한 줄 쓰는 것)
        xssfCell = xssfRow.createCell((short) 0);      // 열 객채 생성 (param = 열의 위치)
        xssfCell.setCellStyle(mainTitleStyle);         // 셀에 스타일 지정
        xssfCell.setCellValue("이름별 통계");           // 데이터 입력




        //============= 엑셀 데이터 입력 (이름별 통계 ===============
        List <Object[]> excelDataList = new ArrayList<>();

        // table head
        excelDataList.add( new Object[]{"이름", "금액"});

        // table data
        if(statisticsMap.get("member") != null) {

            List <Map<String, Object>> nameStatisticsList = (List<Map<String, Object>>) statisticsMap.get("member");

            for (Map<String, Object> nameStatisticsMap : nameStatisticsList)
                excelDataList.add(new Object[]{nameStatisticsMap.get("name"), nameStatisticsMap.get("money")});


            // Data append
            for(Object[] excelData : excelDataList){

                xssfRow = statisticsSheet.createRow(rowNo++); // 행 추가
                cellNo = 0; // 열 index 초기화

                for (Object item : excelData) {
                    xssfCell = xssfRow.createCell(cellNo++);
                    xssfCell.setCellStyle(tableStyle);

                    if (item instanceof String)
                        xssfCell.setCellValue((String) item);
                    else if (item instanceof Integer)
                        xssfCell.setCellValue((Integer) item);
                }
            }
        }


        // 빈 행 추가
        statisticsSheet.createRow(rowNo++);
        xssfRow = statisticsSheet.createRow(rowNo++);
        statisticsSheet.createRow(rowNo++);
        xssfRow = statisticsSheet.createRow(rowNo++);



        //============= 메인 타이틀 만들기 (헌금 종류별 통계) ===============
        // 셀 병합 (첫행, 마지막행, 첫열, 마지막열)
        statisticsSheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo+1, 0, 1));

        // 타이틀 생성
        xssfRow = statisticsSheet.createRow(rowNo++);  // 행 객체 생성 (생성하면 한 줄 쓰는 것)
        xssfCell = xssfRow.createCell((short) 0);      // 열 객채 생성 (param = 열의 위치)
        xssfCell.setCellStyle(mainTitleStyle);         // 셀에 스타일 지정
        xssfCell.setCellValue("헌금종류별 통계");        // 데이터 입력



        //============= 엑셀 데이터 입력 (이름별 통계 ===============
        excelDataList = new ArrayList<>();

        // table head
        excelDataList.add( new Object[]{"헌금종륲", "금액"});

        // table data
        if(statisticsMap.get("offeringKind") != null) {

            List <Map<String, Object>> offeringKindStatisticsList = (List<Map<String, Object>>) statisticsMap.get("offeringKind");

            for (Map<String, Object> offeringKindStatisticsMap : offeringKindStatisticsList)
                excelDataList.add(new Object[]{ offeringKindConverter((String) offeringKindStatisticsMap.get("offeringKind")), offeringKindStatisticsMap.get("money")});


            // Data append
            for(Object[] excelData : excelDataList){

                xssfRow = statisticsSheet.createRow(rowNo++); // 행 추가
                cellNo = 0; // 열 index 초기화

                for (Object item : excelData) {
                    xssfCell = xssfRow.createCell(cellNo++);
                    xssfCell.setCellStyle(tableStyle);

                    if (item instanceof String)
                        xssfCell.setCellValue((String) item);
                    else if (item instanceof Integer)
                        xssfCell.setCellValue((Integer) item);
                }
            }
        }
    }






    /**************************************
     *        헌금 종류 한글 컨버터
     **************************************/
    public String offeringKindConverter(String offeringKind){

        switch(offeringKind){
            case "SUNDAY"       : return "주정헌금";
            case "TITHE"        : return "십일조";
            case "THANKSGIVING" : return "감사헌금";
            case "BUILDING"     : return "건축헌금";
            case "SPECIAL"      : return "특별헌금";
            case "MISSION"      : return "선교헌금";
            case "UNKNOWN"      : return "기타헌금";
            case "TOTAL"        : return "TOTAL" ;
        }
        return "";
    }


    /**************************************
     *        검색기간 컨버터
     **************************************/
    public static String searchDateConverter(SearchDto searchDto){

        LocalDate startDate = searchDto.getStartDate();
        LocalDate endDate = searchDto.getEndDate();
        String returnDate = "";

        if(startDate != null && endDate == null)
            returnDate = " (" + startDate + "~" + LocalDate.now() + ")";
        else if(startDate != null && endDate !=null)
            returnDate = " (" + startDate + "~" + endDate + ")";

        return returnDate;
    }

    private void uploadFolderSetting (){
        File folder = new File(fileUploadPath + "/excel");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }


    public SearchDto makeSearchDto (HashMap<String, Object> map) {

        return SearchDto.builder().searchId((String) map.get("searchId"))
                .searchData((String) map.get("searchData"))
                .startDate(localDateFormatter((String) map.get("startDate")))
                .endDate(localDateFormatter((String) map.get("endDate")))
                .build();
    }


}
