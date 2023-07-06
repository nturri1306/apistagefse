package apistagefse.gateway.service;

import apistagefse.gateway.data.response.DocumentBaseResponse;
import apistagefse.route.rest.service.helper.EndPoint;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.net.URI;

/**
 * @author n.turri
 */

public class GWPublishService extends GWBaseService {

    public ResponseEntity<DocumentBaseResponse> validation(String requestBody, File file) throws Exception {

        var httpClient = getCloseableHttpClient();

        HttpPost httpPost = new HttpPost(getUrlValidation());

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + getBearerToken());
        httpPost.setHeader("FSE-JWT-Signature", getHashSignature());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        StringBody jsonBody = new StringBody(requestBody, ContentType.APPLICATION_JSON);

        FileBody fileBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA, file.getName());
        builder.addPart("file", fileBody);
        builder.addPart("requestBody", jsonBody);

        httpPost.setEntity(builder.build());

        var response = httpClient.execute(httpPost);

        var resp = getResponse(response);

        response.close();

        httpClient.close();

        return resp;
    }


    public ResponseEntity<DocumentBaseResponse> create(String requestBody, File file) throws Exception {


        try (CloseableHttpClient httpClient = getCloseableHttpClient()) {
            HttpPost httpPost = header(new HttpPost(getUrlValidationAndCreate()));

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader(EndPoint.HEADER_AUTHORIZATION, "Bearer " + getBearerToken());
            httpPost.setHeader(EndPoint.HEADER_FSE_JWT_SIGNATURE, getHashSignature());


            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            StringBody jsonBody = new StringBody(requestBody, ContentType.APPLICATION_JSON);

            FileBody fileBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA, file.getName());


            builder.addPart("file", fileBody);
            builder.addPart("requestBody", jsonBody);

            httpPost.setEntity(builder.build());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                return getResponse(response);
            }
        }
    }

    public ResponseEntity<DocumentBaseResponse> updateDocument(String documentId,
                                                               String requestBody,
                                                               File file) throws Exception {


        try (CloseableHttpClient httpClient = getCloseableHttpClient()) {
            HttpPut httpPut = header(new HttpPut(getUrlUpdateDocument(documentId)));

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            StringBody jsonBody = new StringBody(requestBody, ContentType.APPLICATION_JSON);

            FileBody fileBody = new FileBody(file, ContentType.MULTIPART_FORM_DATA, file.getName());
            builder.addPart("file", fileBody);
            builder.addPart("requestBody", jsonBody);

            httpPut.setEntity(builder.build());

            URI uri = new URIBuilder(httpPut.getURI()).build();
            httpPut.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                return getResponse(response);
            }
        }
    }


    public ResponseEntity<DocumentBaseResponse> updateMetadata(String documentId, String requestBody) throws Exception {

        try (CloseableHttpClient httpClient = getCloseableHttpClient()) {
            StringEntity entity = new StringEntity(
                    requestBody, ContentType.APPLICATION_JSON);

            HttpUriRequest request = RequestBuilder.put()
                    .setUri(getUrlUpdateMetaData(documentId))
                    .setEntity(entity)
                    .addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType())
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Bearer " + getBearerToken())
                    .addHeader("FSE-JWT-Signature", getHashSignature())
                    .build();

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                return getResponse(response);
            }
        }
    }

    public ResponseEntity<DocumentBaseResponse> deleteDocument(String documentId) throws Exception {
        try (CloseableHttpClient httpClient = getCloseableHttpClient()) {

            HttpDelete httpDelete = header(new HttpDelete(getUrlDeleteDocument(documentId)));

            URI uri = new URIBuilder(httpDelete.getURI()).build();
            httpDelete.setURI(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                return getResponse(response);
            }
        }
    }

}
