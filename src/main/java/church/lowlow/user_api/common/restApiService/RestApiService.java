package church.lowlow.user_api.common.restApiService;

import java.util.Map;

public interface RestApiService {

    Map<String, Object> getRestApiMap(String urlData, String method, Map<String, Object> requestMap);

    String getUrlForJSON(String url, String method, Map<String, Object> requestMap);
}
