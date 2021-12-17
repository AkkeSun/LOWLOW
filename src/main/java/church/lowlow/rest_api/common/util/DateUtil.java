package church.lowlow.rest_api.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DateUtil {

    private static LocalDate nowDate = LocalDate.now();
    private static LocalDateTime nowDateTime= LocalDateTime.now();

    /***********************
     *오늘날짜 구하기
     *@parampattern: yyyy-MM-dd  / yyyy.MM.dd / yyyyMMdd
     *@returnString
     *********************/
    public static String currentDate(String pattern){
        return nowDate.format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String currentDate(){
        return nowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**********************
     *오늘날짜와 시간 구하기
     *@parampattern: yyyy-MM-dd HH:mm:ss / yyyyMMddHHmmss
     *********************/
    public static String currentDateTime(String pattern){
        return nowDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String currentDateTime(){
        return nowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**********************
     *오늘 요일 구하기
     *@paramtype: eng(MONDAY-), kor(월-), num(1-)
     *********************/

    public static String currentDayOfWeek(String type){
        String dayOfWeek = "";
        switch (type.toUpperCase(Locale.ROOT)) {
            case "ENG" : dayOfWeek =nowDate.getDayOfWeek() + ""; break;
            case "KOR" : dayOfWeek =currentDayOfWeekKor(); break;
            case "NUM" : dayOfWeek =currentDayOfWeekNumber() + ""; break;
            default    : dayOfWeek =nowDate.getDayOfWeek() + ""; break;
        }
        return dayOfWeek;
    }
    public static String currentDayOfWeek(){
        String dayOfWeek = "";
        return nowDate.getDayOfWeek() + "";
    }

    public static String currentDayOfWeekKor(){
        String dayOfWeekKor = "";

        switch (currentDayOfWeekNumber()) {
            case 1: dayOfWeekKor="월";
            case 2: dayOfWeekKor="화";
            case 3: dayOfWeekKor="수";
            case 4: dayOfWeekKor="목";
            case 5: dayOfWeekKor="금";
            case 6: dayOfWeekKor="토";
            case 7: dayOfWeekKor="일";
        }
        return dayOfWeekKor;
    }

    public static int currentDayOfWeekNumber(){
        return nowDate.getDayOfWeek().getValue();
    }

    /**********************
     *입력받은 날짜 요일 구하기
     *@parammyDate: 2021-12-31 pattern으로 입력
     *********************/
    public static String getDayOfWeek(String myDate){
        LocalDate userDate = LocalDate.parse(dateFormatter(myDate));
        return userDate.getDayOfWeek() + "";
    }

    public static String getDayOfWeek(String date, String type){
        LocalDate userDate = LocalDate.parse(date);
        String dayOfWeek = "";
        switch (type.toUpperCase(Locale.ROOT)) {
            case "ENG" : dayOfWeek = userDate.getDayOfWeek() + ""; break;
            case "KOR" : dayOfWeek =currentDayOfWeekKor(userDate); break;
            case "NUM" : dayOfWeek =currentDayOfWeekNumber(userDate) + ""; break;
            default    : dayOfWeek = userDate.getDayOfWeek() + ""; break;
        }
        return dayOfWeek;
    }

    public static String currentDayOfWeekKor(LocalDate myDate){
        String dayOfWeekKor = "";

        switch (currentDayOfWeekNumber(myDate)) {
            case 1: dayOfWeekKor="월";
            case 2: dayOfWeekKor="화";
            case 3: dayOfWeekKor="수";
            case 4: dayOfWeekKor="목";
            case 5: dayOfWeekKor="금";
            case 6: dayOfWeekKor="토";
            case 7: dayOfWeekKor="일";
        }
        return dayOfWeekKor;
    }

    public static int currentDayOfWeekNumber(LocalDate myDate){
        return myDate.getDayOfWeek().getValue();
    }

    /**********************
     *오늘이 시작일,종료일 사이에 있는지 확인
     *@paramstartDate: yyyyMMdd / yyyy-MM-dd
     *@paramendDate: yyyyMMdd / yyyy-MM-dd
     *@returnboolean
     *********************/
    public static boolean dateCheck(String startDate, String endDate){
        LocalDate sDate = LocalDate.parse(dateFormatter(startDate));
        LocalDate eDate = LocalDate.parse(dateFormatter(endDate));

        long sDays = ChronoUnit.DAYS.between(sDate,nowDate);
        long eDays = ChronoUnit.DAYS.between(eDate,nowDate);

        return  sDays >= 0 && eDays <= 0 ? true : false;
    }

    /**********************
     *오늘이 시작일,종료일 사이에 있는지 확인(시간포함)
     *@paramstartDateTime: yyyyMMddHHmmss / yyyy-MM-dd HH:mm:ss
     *@paramendDateTime: yyyyMMddHHmmss / yyyy-MM-dd HH:mm:ss
     *@returnboolean
     *********************/
    public static boolean dateTimeCheck(String startDateTime, String endDateTime){

        int nowDateTimeforCheck = Integer.parseInt(currentDateTime("yyyyMMddHHmmss"));
        int sDateTime = Integer.parseInt(dateTimeFormatter(startDateTime));
        int eDateTime = Integer.parseInt(dateTimeFormatter(endDateTime));

        return sDateTime >= nowDateTimeforCheck && eDateTime <= nowDateTimeforCheck ? true : false;
    }

    /**********************
     *날짜 형식 포맷
     *@paramuserDate: 20211231
     *@return: 2021-12-31
     *********************/
    public static String dateFormatter(String userDate){
        if(!userDate.contains("-"))
            userDate =  userDate.substring(0,4) + "-"
                    + userDate.substring(4,6) + "-"
                    + userDate.substring(6,8);
        return userDate;
    }

    /**********************
     *날짜 시간 형식 포맷
     *@paramuserDateTime: yyyy-MM-dd HH:mm:ss
     *@return: yyyyMMddHHmmss
     *********************/
    public static String dateTimeFormatter(String userDateTime){
        if(userDateTime.contains("-"))
            userDateTime = userDateTime.replace("-", "");
        if(userDateTime.contains(" "))
            userDateTime = userDateTime.replace(" ", "");
        if(userDateTime.contains(":"))
            userDateTime = userDateTime.replace(":", "");
        return userDateTime;
    }



}