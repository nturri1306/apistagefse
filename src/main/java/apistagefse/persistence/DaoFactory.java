package apistagefse.persistence;

import apistagefse.conf.Fse2BrokerProperties;
import apistagefse.gateway.data.request.*;
import apistagefse.gateway.data.response.BaseResponse;
import apistagefse.gateway.data.response.DocumentBaseResponse;
import apistagefse.gateway.helper.CdaValidator;
import apistagefse.gateway.helper.FseCdaExtractor;
import apistagefse.gateway.helper.JsonHelper;
import apistagefse.persistence.dao.*;
import apistagefse.persistence.entity.BrokerEntity;
import apistagefse.persistence.entity.ClinicalDocumentArchitectureEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Adapter class to entities DAO that allow external to access to
 * DAO and entities
 *
 * @author b.amoruso
 */
@Component
public class DaoFactory {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Fse2BrokerProperties conf;

    @Autowired
    private CreateDao createDao;
    @Autowired
    private PublishDao publishDao;
    @Autowired
    private UpdateDao updateDao;
    @Autowired
    private MetadataDao metadataDao;

    // @Autowired
    // private ValidationDao validationDao;
    @Autowired
    private WorkflowInstanceStatusDao workflowInstanceStatusDao;
    @Autowired
    private TraceIdStatusDao traceIdStatusDao;
    @Autowired
    private DeleteDao deleteDao;

    public void save(CdaValidator validator, CreateRequest request, ResponseEntity<DocumentBaseResponse> response) {
        createDao.save(enrich(validator, toEntity(request, response)));
    }


    public void save(CdaValidator validator, UpdateRequest request, ResponseEntity<DocumentBaseResponse> response) {
        updateDao.save(enrich(validator, toEntity(request, response)));
    }


    public void save(MetadataRequest request, ResponseEntity<DocumentBaseResponse> response) {
        metadataDao.save(toEntity(request, response));
    }

    public void saveDelete(DeleteRequest request, ResponseEntity<DocumentBaseResponse> response) {
        deleteDao.save(toEntity(request, response));
    }

    public void saveTraceId(TraceIdStatusRequest request, ResponseEntity<BaseResponse> response) {
        traceIdStatusDao.save(toEntity(request, response));
    }

    public void saveWorkflowInstanceId(WorkflowInstanceIdStatusRequest request, ResponseEntity<BaseResponse> response) {
        workflowInstanceStatusDao.save(toEntity(request, response));
    }

    private <T extends BrokerEntity, R extends BaseResponse> T
    toEntity(BaseRequest<T> request, ResponseEntity<R> response) {

        BaseResponse r = response.getBody();
        r.setStatusCode(response.getStatusCodeValue());

        T entity = request.toEntity();
        entity.asSent();
        entity.setResponse(r);

        try {
            log.info(JsonHelper.toString(entity, true));
        } catch (JsonProcessingException e) {
            log.error("Unable to print entity " + entity.getId() + " (" + entity.getClass() + ")");
        }

        return entity;
    }

    private <T extends ClinicalDocumentArchitectureEntity> T enrich(CdaValidator validator, T entity) {
        try {
            FseCdaExtractor ex = validator.getExtractor();
            entity.setPatient(ex.getAssistedCode(), ex.getAssistedAuthority());
            entity.setAuthor(ex.getAuthorId(), ex.getAuthorAuthority(), ex.getAuthorTime());
            entity.setStructureId(ex.getDocumentId(true));
            entity.setCustodian(ex.getCustodianId(), ex.getCustodianAuthority());
            entity.setAuthenticator(ex.getAuthenticatorId(), ex.getAuthenticatorAuthority(), ex.getAuthenticatorTime());
            // Record original file...
            entity.setFilename(validator.getOriginalFileName());
            entity.setCda_xml(ex.getCda_xml());
            entity.setCda_json(ex.getCda_json());


        } catch (Exception ex) {
            log.error("Unable to extract data from file", ex);
        }

        return entity;
    }

}
