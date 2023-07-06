package apistagefse.route;

import apistagefse.conf.Fse2BrokerProperties;
import apistagefse.gateway.data.DTO.PublicationMetadataReqDTO;
import apistagefse.gateway.data.DTO.PublicationUpdateReqDTO;
import apistagefse.gateway.data.DTO.ValidationCDAReqDTO;
import apistagefse.gateway.data.DTO.ValidationCreationDTO;
import apistagefse.gateway.data.enums.ActivityEnum;
import apistagefse.gateway.data.enums.HealthDataFormatEnum;
import apistagefse.gateway.data.request.*;
import apistagefse.gateway.data.response.DocumentBaseResponse;
import apistagefse.gateway.data.response.DocumentErrorResponse;
import apistagefse.gateway.data.response.ErrorResponse;
import apistagefse.gateway.helper.CdaValidator;
import apistagefse.gateway.helper.JsonHelper;
import apistagefse.gateway.helper.TokenProvider;
import apistagefse.gateway.service.GWPublishService;
import apistagefse.persistence.DaoFactory;
import apistagefse.route.rest.service.helper.ParamRequest;
import apistagefse.route.rest.service.helper.ServiceHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.UUID;


/**
 * @author b.amoruso
 */
@Component
public class GatewayRoute extends RouteBuilder {

    public static final String CREATE_DOCUMENT = "seda:create_document";
    public static final String UPDATE_DOCUMENT = "seda:update_document";
    public static final String UPDATE_METADATA = "seda:update_metadata";
    public static final String DELETE_DOCUMENT = "seda:delete_document";

    public static final String TRACEID_STATUS = "seda:traceid_status";

    public static final String WORKFLOWINSTANCEID_STATUS = "seda:workflowinstanceid_status";

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private DaoFactory dao;

    @Autowired
    //@Qualifier("fseBrokerConf")
    private Fse2BrokerProperties conf;

    @Autowired
    private ServiceHelper services;

    @Override
    public void configure() throws Exception {


        onException(Exception.class)
                .maximumRedeliveries(conf.getMaximumRedeliveries())
                .onWhen(exception -> exception instanceof ConnectException)
                .maximumRedeliveries(3)
                .redeliveryDelay(1000)
                .process(e ->
                        log.error("Unable to complete request of {} coming from {}",
                                e.getIn().getBody().toString(), e.getFromEndpoint()));

        // Document operations
        configureCreateDocument();
        configureDeleteDocument();
        configureUpdateDocument();
        configureUpdateMetadata();
/*
        // Status operations
        configureGetStatusTraceId();
        configureGetStatusWorkflowInstanceId();*/

    }

    private void configureCreateDocument() {
        from(CREATE_DOCUMENT).
                log("Received a create request").
                process(e -> {
                    ParamRequest req = e.getIn().getBody(ParamRequest.class);

                    ResponseEntity<DocumentBaseResponse> response = null;

                    CreateRequest request = null;

                    File f = null;

                    CdaValidator validator = new CdaValidator(conf.getTempDirectory(), req.getMultipartFile().getBytes());
                    validator.setOriginalFileName(req.getMultipartFile().getOriginalFilename());

                    try {
                        log.info("{}: {}", CREATE_DOCUMENT, req);

                        log.info("body: " + JsonHelper.toString(req.getRequestBody(), true));
                        String body = JsonHelper.toString(req.getRequestBody());

                        var healthData = JsonHelper.from(body, ValidationCreationDTO.class);

                        GWPublishService gwPublishService = new GWPublishService();
                        gwPublishService.setConfig(conf);
                        gwPublishService.generateAndSetToken(
                                TokenProvider.forValidation(new CdaValidator(conf.getTempDirectory(), req.getMultipartFile().getBytes())));

                        String fileName = getFileMultipart(req.getMultipartFile().getBytes(), getExtension(req.getMultipartFile().getOriginalFilename()));

                        f = new File(fileName);

                        request = services.getCreateRequest(validator.getOriginalFileName(),
                                healthData, gwPublishService.getUrlValidationAndCreate(),
                                gwPublishService.getBearerToken(),
                                gwPublishService.getHashSignature());


                        response = gwPublishService.create(body, new File(fileName));

                        e.getMessage().setBody(response(response.getBody(), response.getStatusCode()));

                    } catch (Exception ex) {
                        log.info("error:" + ex.getMessage());

                        if (request == null) {
                            request = new CreateRequest();
                            response = getErrorResponse(ex.getMessage());
                        }
                        e.getMessage().setBody(response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                    } finally {
                        dao.save(validator, request, response);
                        if (f != null)
                            f.delete();
                    }
                });
    }

    private void configureUpdateDocument() {
        from(UPDATE_DOCUMENT).
                log("Received a create request").
                process(e -> {
                    ParamRequest req = e.getIn().getBody(ParamRequest.class);

                    ResponseEntity<DocumentBaseResponse> response = null;

                    UpdateRequest request = null;

                    File f = null;

                    CdaValidator validator = new CdaValidator(conf.getTempDirectory(), req.getMultipartFile().getBytes());
                    validator.setOriginalFileName(req.getMultipartFile().getOriginalFilename());

                    try {
                        log.info("{}: {}", UPDATE_DOCUMENT, req);
                        log.info("body: " + JsonHelper.toString(req.getRequestBody(), true));
                        String body = JsonHelper.toString(req.getRequestBody());

                        var healthData = JsonHelper.from(body, PublicationUpdateReqDTO.class);

                        GWPublishService gwPublishService = new GWPublishService();
                        gwPublishService.setConfig(conf);
                        gwPublishService.generateAndSetToken(
                                TokenProvider.forValidation(new CdaValidator(conf.getTempDirectory(), req.getMultipartFile().getBytes())));

                        String fileName = getFileMultipart(req.getMultipartFile().getBytes(), getExtension(req.getMultipartFile().getOriginalFilename()));

                        f = new File(fileName);

                        request = services.getUpdateRequest(validator.getOriginalFileName(),
                                healthData, gwPublishService.getUrlUpdateDocument(healthData.getIdentificativoDoc()),
                                gwPublishService.getBearerToken(),
                                gwPublishService.getHashSignature());

                        ValidationCDAReqDTO validationReq = ValidationCDAReqDTO.builder().
                                activity(ActivityEnum.VALIDATION).
                                mode(healthData.getMode()).
                                healthDataFormat(HealthDataFormatEnum.CDA).
                                build();

                        var _response = gwPublishService.validation(JsonHelper.toString(validationReq), new File(fileName));

                        response = gwPublishService.updateDocument(req.getDocumentId(), body, new File(fileName));

                        e.getMessage().setBody(response(response.getBody(), response.getStatusCode()));

                    } catch (Exception ex) {
                        log.info("error:" + ex.getMessage());

                        if (request == null) {
                            request = new UpdateRequest();
                            response = getErrorResponse(ex.getMessage());
                        }

                        e.getMessage().setBody(response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                    } finally {
                        dao.save(validator, request, response);
                        if (f != null)
                            f.delete();
                    }
                });
    }

    private void configureUpdateMetadata() {
        from(UPDATE_METADATA).
                log("Received an update metadata request").
                process(e -> {
                    ParamRequest req = e.getIn().getBody(ParamRequest.class);

                    ResponseEntity<DocumentBaseResponse> response = null;

                    MetadataRequest request = null;

                    try {
                        log.info("{}: {}", UPDATE_METADATA, req);

                        log.info("body: " + JsonHelper.toString(req.getRequestBody(), true));
                        String body = JsonHelper.toString(req.getRequestBody());

                        var healthData = JsonHelper.from(body, PublicationMetadataReqDTO.class);

                        GWPublishService gwPublishService = new GWPublishService();
                        gwPublishService.setConfig(conf);
                        gwPublishService.generateAndSetToken(TokenProvider.makeUpdateParam());

                        request = services.getMetadataRequest(
                                healthData, gwPublishService.getUrlUpdateMetaData(req.getDocumentId()),
                                gwPublishService.getBearerToken(),
                                gwPublishService.getHashSignature());


                        response = gwPublishService.updateMetadata(req.getDocumentId(), body);

                        e.getMessage().setBody(response(response.getBody(), response.getStatusCode()));

                    } catch (Exception ex) {
                        log.info("error:" + ex.getMessage());

                        if (request == null) {
                            request = new MetadataRequest();
                            response = getErrorResponse(ex.getMessage());
                        }

                        e.getMessage().setBody(response(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
                    } finally {
                        dao.save(request, response);

                    }
                });
    }

    private void configureDeleteDocument() {
        from(DELETE_DOCUMENT).
                log("Received a delete document request").
                process(e -> {
                    ParamRequest req = e.getIn().getBody(ParamRequest.class);

                    DeleteRequest request = null;

                    ResponseEntity<DocumentBaseResponse> response = null;

                    try {
                        log.info("{}: {}", DELETE_DOCUMENT, req.getDocumentId());

                        GWPublishService gwPublishService = new GWPublishService();
                        gwPublishService.setConfig(conf);
                        gwPublishService.generateAndSetToken(TokenProvider.makeDeleteParam());

                        request = services.getDeleteRequest(gwPublishService.getUrlDeleteDocument(req.getDocumentId()),
                                gwPublishService.getBearerToken(),
                                gwPublishService.getHashSignature());

                        response = gwPublishService.deleteDocument(req.getDocumentId());

                        e.getMessage().setBody(response(response.getBody(), response.getStatusCode()));

                    } catch (Exception ex) {
                        log.info(ex.getMessage());

                        if (request == null) {
                            request = new DeleteRequest();
                            response = getErrorResponse(ex.getMessage());
                        }
                    } finally {
                        dao.saveDelete(request, response);

                    }
                });
    }


    @Getter
    @Setter
    public class ExceptionResponse {
        private ResponseEntity<DocumentErrorResponse> responseEntity;

        private BaseRequest request;

        public ExceptionResponse(BaseRequest request, ResponseEntity<DocumentErrorResponse> responseEntity) {
            this.request = request;
            this.responseEntity = responseEntity;
        }


    }


    private String getFileMultipart(byte[] fileBytes, String ext) {
        try {
            String fileName = UUID.randomUUID().toString() + "." + ext;
            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(fileBytes);
            fos.close();

            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // Restituisce null in caso di errore
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return null;
        }

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }


    public static <R> ResponseEntity<R> response(R entity, HttpStatus status) {
        try {
            return ResponseEntity.status(status).body(entity);
        } catch (Exception ex) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(entity);
        }
    }

  /*  public static ErrorResponseDTO getErrorResponse(String detail, int statusCode) {

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(null, null, null, "Internal error", detail, statusCode, null);
        errorResponse.setDetail(detail);
        errorResponse.setStatus(statusCode);

        return errorResponse;
    }*/

    public static ResponseEntity<DocumentBaseResponse> getErrorResponse(String error) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(error);
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}







