package church.lowlow.rest_api.common.util;

import java.util.regex.Pattern;

public class StringUtil {

    private static final Pattern IS_ONLY_NUMBER = Pattern.compile("^[0-9]*?");

    /**********************
     * Null Check
     *@paramstr: null || ""
     *@return replaceStr
     *********************/
    public static String objNullToStr(String str, String replaceStr){
        if(str == null || str.equals(""))
            str = replaceStr;
        return str;
    }
    public static String objNullToStr(String str){
        if(str == null || str.equals(""))
            str = "";
        return str;
    }
    public static boolean StringNullCheck(String str){
        if(str == null || str.equals(""))
            return true;
        return false;
    }



    /**********************
     *문자열을int로 형변환
     *@return:예외 발생 시-1출력
     *********************/
    public static int parseInteger(String str){
        String nums = "/[^0-9]/g";

        if(str == null || str.equals("") || !IS_ONLY_NUMBER.matcher(str).matches())
            return -1;
        return Integer.parseInt(str);
    }


    /**********************
     *문자열을Long으로 형변환
     *@return:예외 발생 시-1L출력
     *********************/
    public static Long parseLong(String str){
        String nums = "/[^0-9]/g";

        if(str == null || str.equals("") || !IS_ONLY_NUMBER.matcher(str).matches())
            return -1L;
        return Long.parseLong(str);
    }

}
