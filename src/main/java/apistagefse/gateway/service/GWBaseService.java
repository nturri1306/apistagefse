package apistagefse.gateway.service;


import apistagefse.conf.Fse2BrokerProperties;
import apistagefse.gateway.data.ParamToken;
import apistagefse.gateway.data.response.DocumentBaseResponse;
import apistagefse.gateway.data.response.DocumentErrorResponse;
import apistagefse.gateway.data.response.DocumentSuccessResponse;
import apistagefse.gateway.data.response.ErrorResponse;
import apistagefse.gateway.helper.JWTGenerator;
import apistagefse.gateway.helper.JsonHelper;
import apistagefse.gateway.helper.TokenProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @author n.turri
 */
public class GWBaseService {

    private static final Logger log = LoggerFactory.getLogger(GWBaseService.class);

    private Fse2BrokerProperties conf;
    private String bearerToken;
    private String hashSignature;
    private String urlService;

    protected String urlValidation = "/v1/documents/validation";
    protected String urlPublish = "/v1/documents";

    public String getUrlValidation() {
        return getUrlService() + urlValidation;
    }

    public String getUrlPublish() {
        return getUrlService() + urlPublish;
    }

    protected String urlSearchByTraceId = "/v1/status/search";
    protected String urlSearchByWorkstanceId = "/v1/status";

    protected String urlValidationAndCreate = "/v1/documents/validate-and-create";

    public String getUrlUpdateDocument(String documentId) {
        return getUrlPublish() + "/" + documentId;
    }

    public String getUrlDeleteDocument(String documentId) {
        return getUrlPublish() + "/" + documentId;
    }

    public String getUrlValidationAndCreate() {
        return getUrlService() + urlValidationAndCreate;
    }

    public String getUrlUpdateMetaData(String documentId) {
        return getUrlPublish() + "/metadata/" + documentId;
    }

    public String getUrlSearchByTraceId(String traceId) {
        return getUrlService() + urlSearchByTraceId + "/" + traceId;
    }

    public String getUrlSearchByWorkstanceId(String workstanceId) {
        return getUrlService() + urlSearchByWorkstanceId + "/" + workstanceId;
    }

    public void setHashSignature(String hashSignature) {
        this.hashSignature = hashSignature;
    }

    public String getHashSignature() {
        return hashSignature;
    }

    public String getBearerToken() {
        return bearerToken.trim();
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getUrlService() {
        return urlService;
    }

    public void setUrlService(String urlService) {
        this.urlService = urlService;
    }

    public void setConfig(Fse2BrokerProperties conf) {
        this.conf = conf;

        setUrlService(conf.getGwUrlService());
    }


    public CloseableHttpClient getCloseableHttpClient() throws Exception {

        InputStream stream = null;

        try {

            stream = new FileInputStream(conf.getTrustStoreFile());
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream, conf.getTrustPassword().toCharArray());

            stream.close();

            SSLContext sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, conf.getTrustPassword().toCharArray()).build();
            SSLConnectionSocketFactory sslConFactory = new SSLConnectionSocketFactory(sslContext);

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConFactory).build();

            return httpClient;


        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex.getMessage());

        } finally {

            if (stream != null)
                stream.close();

        }


    }

    public RestTemplate getRestTemplate() throws Exception {
        RestTemplate restTemplate;

        if (urlService.indexOf("https") < 0) {
            restTemplate = new RestTemplate();
        } else {
            restTemplate = getSSLRestTemplate();
        }

        return restTemplate;
    }

    public RestTemplate getSSLRestTemplate() throws Exception {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(getCloseableHttpClient());

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        return restTemplate;
    }

    public void generateAndSetToken(ParamToken paramToken) throws Exception {
        paramToken.setFileBaseSign(conf.getFileBaseSign());

        String fileJson = TokenProvider.getFileDataSign(
                paramToken, conf.getTempDirectory(), conf.getAssigningAuthority());
        //generate token jwt
        JWTGenerator jwtGenerator = new JWTGenerator();
        jwtGenerator.sign_generate_with_hash(fileJson,
                paramToken.getPdfFile(), conf.getUserSignature(), conf.getTrustPassword());


        setBearerToken(jwtGenerator.getJwt());
        setHashSignature(jwtGenerator.getClaimsJwt());

        if (!new File(fileJson).delete()) {

        }
    }


    public ResponseEntity<DocumentBaseResponse> getResponse(CloseableHttpResponse response) throws IOException {
        String jsonString = EntityUtils.toString(response.getEntity());

        log.info(JsonHelper.toString(jsonString, true));

        boolean success = response.getStatusLine().getStatusCode() == 200 ||
                response.getStatusLine().getStatusCode() == 201;

        try {
            return new ResponseEntity<>(success ?
                    JsonHelper.from(jsonString, DocumentSuccessResponse.class) :
                    JsonHelper.from(jsonString, DocumentErrorResponse.class),
                    HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
        } catch (Exception ex) {

            return new ResponseEntity<>(JsonHelper.from(jsonString, ErrorResponse.class),
                    HttpStatus.valueOf(response.getStatusLine().getStatusCode()));

        }

    }

    protected <T extends HttpRequestBase> T header(T http) {
        http.setHeader("Accept", "application/json");
        http.setHeader("Authorization", "Bearer " + getBearerToken());
        http.setHeader("FSE-JWT-Signature", getHashSignature());
        return http;
    }

}
