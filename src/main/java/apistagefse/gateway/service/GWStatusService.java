package apistagefse.gateway.service;

import apistagefse.gateway.data.DocumentStatus;
import apistagefse.gateway.data.response.BaseResponse;
import apistagefse.gateway.data.response.DocumentErrorResponse;
import apistagefse.gateway.data.response.TransactionDataResponse;
import apistagefse.gateway.helper.JsonHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author n.turri
 */
public class GWStatusService extends GWBaseService {

    private static final Logger log = LoggerFactory.getLogger(GWStatusService .class);

    public ResponseEntity<DocumentStatus> getStatus() {
        try {
            return getRestTemplate().getForEntity(getUrlService() + "/status", DocumentStatus.class);
        } catch (Exception ex) {
            return null;
        }
    }

    public ResponseEntity<BaseResponse> getStatusTraceId(String traceId) throws Exception {

        var httpClient = getCloseableHttpClient();

        HttpGet httpGet = new HttpGet(getUrlSearchByTraceId(traceId));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getBearerToken());

        URI uri = new URIBuilder(httpGet.getURI())
                //.addParameters(nameValuePairs)
                .build();
        httpGet.setURI(uri);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String jsonString = EntityUtils.toString(response.getEntity());

        log.info( JsonHelper.toString(jsonString,true));

        boolean success = response.getStatusLine().getStatusCode() == 200 ||
                response.getStatusLine().getStatusCode() == 201;

        return new ResponseEntity<>(success ?
                JsonHelper.from(jsonString, TransactionDataResponse.class) :
                JsonHelper.from(jsonString, DocumentErrorResponse.class),
                HttpStatus.valueOf(response.getStatusLine().getStatusCode()));

    }

    public ResponseEntity<BaseResponse> getWorkflowInstanceId(String workflowInstanceId) throws Exception {

        var httpClient = getCloseableHttpClient();

        HttpGet httpGet = new HttpGet(getUrlSearchByWorkstanceId(encodeValue(workflowInstanceId)));
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Authorization", "Bearer " + getBearerToken());

        URI uri = new URIBuilder(httpGet.getURI())
                //.addParameters(nameValuePairs)
                .build();
        httpGet.setURI(uri);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        String jsonString = EntityUtils.toString(response.getEntity());

        log.info( JsonHelper.toString(jsonString,true));

        boolean success = response.getStatusLine().getStatusCode() == 200 ||
                response.getStatusLine().getStatusCode() == 201;

        return new ResponseEntity<>(success ?
                JsonHelper.from(jsonString, TransactionDataResponse.class) :
                JsonHelper.from(jsonString, DocumentErrorResponse.class),
                HttpStatus.valueOf(response.getStatusLine().getStatusCode()));


    }

    private String encodeValue(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }


}
