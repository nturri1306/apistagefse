package apistagefse.route.rest.service.helper;


import apistagefse.CdaExtractor;
import apistagefse.conf.Fse2BrokerProperties;
import apistagefse.gateway.data.DTO.PublicationMetadataReqDTO;
import apistagefse.gateway.data.DTO.PublicationUpdateReqDTO;
import apistagefse.gateway.data.DTO.ValidationCreationDTO;
import apistagefse.gateway.data.*;
import apistagefse.gateway.data.request.*;
import apistagefse.gateway.helper.CdaValidator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class ServiceHelper {

    @Autowired
    private Fse2BrokerProperties conf;

    @Getter
    @Setter
    public class ValidateParam {

        private StringBuffer result;
        private File file;

    }

    public ValidateParam validate(CdaValidator validator) throws Exception {
        ValidateParam validateParam = new ValidateParam();

        CdaExtractor cdaExtractor = validator.getExtractor();
        String typeCode = cdaExtractor.getTypeCode();

        StringBuffer result = new StringBuffer();

        if (conf.isValidation()) {
            result = validator.validateCda(
                    conf.getSchematronPath(), cdaExtractor.getContent(), SchemaCode.getByCode(typeCode).getDocumentType());
        } else {
            result.append("true");
        }

        validateParam.setResult(result);
        validateParam.setFile(validator.getSource());

        return validateParam;
    }

    public StringBuffer validateCDA(byte[] bytes) throws Exception {
        CdaValidator cda = new CdaValidator(conf.getTempDirectory(), bytes);
        CdaExtractor cdaEx = cda.getExtractor();
        String typeCode = cdaEx.getTypeCode();

        return cda.validateCda(
                conf.getSchematronPath(), cdaEx.getContent(), SchemaCode.getByCode(typeCode).getDocumentType());
    }

    public UpdateRequest getUpdateRequest(String fileName,
                                          PublicationUpdateReqDTO requestBody,
                                          String url,
                                          String bearerToken,
                                          String claimsToken) {

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setBearerToken(bearerToken);
        updateRequest.setClaimsToken(claimsToken);
        updateRequest.setFileName(fileName);
        updateRequest.setRequestBody(requestBody);
        updateRequest.setServiceUrl(url);

        return updateRequest;
    }

    public MetadataRequest getMetadataRequest(PublicationMetadataReqDTO requestBody,
                                              String url,
                                              String bearerToken,
                                              String claimsToken) {

        MetadataRequest metaDataRequest = new MetadataRequest();
        metaDataRequest.setBearerToken(bearerToken);
        metaDataRequest.setClaimsToken(claimsToken);
        metaDataRequest.setServiceUrl(url);
        metaDataRequest.setRequestBody(requestBody);

        return metaDataRequest;
    }

    public ValidationRequest getValidationRequest(String fileName,
                                                  HealthDataValidate requestBody,
                                                  String url,
                                                  String bearerToken,
                                                  String claimsToken) {

        ValidationRequest validation = new ValidationRequest();
        validation.setBearerToken(bearerToken);
        validation.setClaimsToken(claimsToken);
        validation.setFileName(fileName);
        validation.setRequestBody(requestBody);
        validation.setServiceUrl(url);

        return validation;
    }


    public CreateRequest getCreateRequest(String fileName,
                                          ValidationCreationDTO requestBody,
                                          String url,
                                          String bearerToken,
                                          String claimsToken) {

        CreateRequest createRequest = new CreateRequest();
        createRequest.setBearerToken(bearerToken);
        createRequest.setClaimsToken(claimsToken);
        createRequest.setFileName(fileName);
        createRequest.setRequestBody(requestBody);
        createRequest.setServiceUrl(url);

        return createRequest;
    }


    public DeleteRequest getDeleteRequest(String url, String bearerToken, String claimsToken) {
        DeleteRequest request = new DeleteRequest();
        request.setBearerToken(bearerToken);
        request.setClaimsToken(claimsToken);
        request.setServiceUrl(url);

        return request;
    }

    public WorkflowInstanceIdStatusRequest getWorkflowInstanceIdStatusRequest(String url, String bearerToken, String claimsToken) {
        WorkflowInstanceIdStatusRequest request = new WorkflowInstanceIdStatusRequest();
        request.setBearerToken(bearerToken);
        request.setClaimsToken(claimsToken);
        request.setServiceUrl(url);

        return request;
    }

    public TraceIdStatusRequest getTraceIdStatusRequest(String url, String bearerToken, String claimsToken) {
        TraceIdStatusRequest request = new TraceIdStatusRequest();
        request.setBearerToken(bearerToken);
        request.setClaimsToken(claimsToken);
        request.setServiceUrl(url);

        return request;
    }


}
