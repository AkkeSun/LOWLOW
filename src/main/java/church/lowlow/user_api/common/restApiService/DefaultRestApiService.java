package church.lowlow.user_api.common.restApiService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@Service
@Log4j2
public class DefaultRestApiService implements RestApiService {

    @Value("${restApiBaseUrl}")
    private String BASE_URL;


    @Override
    public Map<String, Object> getRestApiMap(String urlData, String method, Map<String, Object> requestMap)  {

        Map<String, Object> restApiMap = null;

        try {
            String urlForJSON = getUrlForJSON(urlData, method, requestMap);
            ObjectMapper om = new ObjectMapper();
            restApiMap = om.readValue(urlForJSON, new TypeReference<Map<String, Object>>(){});
        } catch(IOException e) {
            e.printStackTrace();
        }

        return restApiMap;
    }




    @Override
    public String getUrlForJSON(String urlData, String method, Map<String, Object> requestMap) {

            String responseData = "";

            StringBuffer sb = new StringBuffer();

            try {
                URL url = new URL(BASE_URL + urlData.trim());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod(method); // 메소드 타입
                conn.setDoOutput(true);        // 통신 후 데이터를 출력할 수 있도록 설정
                conn.setConnectTimeout(5000);  // 연결 제한시간 (5초)
                conn.setRequestProperty("Content-Type", "application/json; utf-8"); // 요청 타입
                conn.setRequestProperty("Accept", "application/hal+json; utf-8");   // 응답 타입


                // Request Body Setting
                if(requestMap != null) {
                    String requestBody = getJsonStringFromMap(requestMap);
                    System.out.println("requestBody : " + requestBody);

                    try(OutputStream os = conn.getOutputStream()) {
                        byte[] input = requestBody.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }
                }

                //http 요청 실시
                conn.connect();

                //http 요청 후 응답 받은 데이터를 버퍼에 쌓기
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                while ((responseData = br.readLine()) != null) {
                    sb.append(responseData);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

    private static String getJsonStringFromMap(Map<String, Object> map) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(map);
    }



}
