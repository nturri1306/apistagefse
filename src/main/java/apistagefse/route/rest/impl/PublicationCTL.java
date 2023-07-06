package apistagefse.route.rest.impl;


import apistagefse.conf.Fse2BrokerProperties;
import apistagefse.gateway.data.DTO.PublicationMetadataReqDTO;
import apistagefse.gateway.data.DTO.PublicationUpdateReqDTO;
import apistagefse.gateway.data.DTO.ValidationCreationDTO;
import apistagefse.gateway.data.response.DocumentBaseResponse;
import apistagefse.gateway.helper.JsonHelper;
import apistagefse.route.GatewayRoute;
import apistagefse.route.rest.IPublicationCTL;
import apistagefse.route.rest.basic.PlainUser;
import apistagefse.route.rest.service.helper.ParamRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


@RestController
public class PublicationCTL extends AbstractCTL implements IPublicationCTL {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Fse2BrokerProperties conf;
    @Autowired
    ProducerTemplate producer;


    @Override
    @Operation(summary = "Validazione e creazione documenti", description = "VALIDAZIONE E PUBBLICAZIONE CREAZIONE CONTESTUALE", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<DocumentBaseResponse> validateAndCreate(@RequestParam(name = "requestBody") Object requestBody, @RequestParam(name = "file") MultipartFile file, HttpServletRequest request) {


        String methodName = MethodUtils.getAccessibleMethod(getClass(), "validateAndCreate", Object.class, MultipartFile.class, HttpServletRequest.class).toString();

        log.info("Begin " + getCallingMethodName());

        try {

            String auth = request.getHeader("Authorization");

            if (!authenticate(conf, request, auth)) {
                log.info("Unauthorized : " + auth);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //temporary the class must arrive already valued
            var healthData = JsonHelper.from(String.valueOf(requestBody), ValidationCreationDTO.class);

            var response = producer.requestBody(GatewayRoute.CREATE_DOCUMENT, new ParamRequest(healthData, file), ResponseEntity.class);

            log.info("Response Status: " + response.getStatusCode());
            log.info("Response Body: " + JsonHelper.toString(response.getBody()));

            return response;


        } catch (Exception e) {
            log.error(e.getMessage());
            return GatewayRoute.getErrorResponse(e.getMessage());

        } finally {
            log.info("End " + methodName);
        }


    }

    @Override
    @Operation(summary = "Pubblicazione replace documenti", description = "Sostituisce il documento identificato da oid fornito in input.", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<DocumentBaseResponse> replace(String idDoc, @RequestParam(name = "requestBody") Object requestBody, MultipartFile file, HttpServletRequest request) {


        String methodName = MethodUtils.getAccessibleMethod(getClass(), "replace", String.class, Object.class, MultipartFile.class, HttpServletRequest.class).toString();

        log.info("Begin " + getCallingMethodName());

        try {
            String auth = request.getHeader("Authorization");

            if (!authenticate(conf, request, auth)) {
                log.info("Unauthorized : " + auth);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //temporary the class must arrive already valued
            var healthData = JsonHelper.from(String.valueOf(requestBody), PublicationUpdateReqDTO.class);

            var response = producer.requestBody(GatewayRoute.UPDATE_DOCUMENT, new ParamRequest(encode(idDoc), healthData, file), ResponseEntity.class);

            log.info("Response Status: " + response.getStatusCode());
            log.info("Response Body: " + JsonHelper.toString(response.getBody()));

            return response;


        } catch (Exception e) {
            log.error(e.getMessage());
            return GatewayRoute.getErrorResponse(e.getMessage());
        } finally {
            log.info("End " + methodName);
        }
    }


    @Override
    @Operation(summary = "Eliminazione documento", description = "Elimina il documento identificato da oid fornito in input.", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<DocumentBaseResponse> delete(String id, HttpServletRequest request) {

        String methodName = MethodUtils.getAccessibleMethod(getClass(), "delete", String.class, HttpServletRequest.class).toString();


        try {
            String auth = request.getHeader("Authorization");

            log.info("Begin " + getCallingMethodName());

            if (!authenticate(conf, request, auth)) {
                log.info("Unauthorized: " + auth);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }


            log.info("Delete of document \"{}\"...", id);

            var response = producer.requestBody(GatewayRoute.DELETE_DOCUMENT, new ParamRequest(encode(id)), ResponseEntity.class);

            log.info("Response Status: " + response.getStatusCode());
            log.info("Response Body: " + JsonHelper.toString(response.getBody()));

            return response;


        } catch (Exception e) {
            log.error(e.getMessage());
            return GatewayRoute.getErrorResponse(e.getMessage());
        } finally {
            log.info("End " + methodName);
        }

    }

    @Override

    @Operation(summary = "Pubblicazione aggiornamento metadati", description = "Pubblicazione aggiornamento metadati dato l'identificativo documento.", security = @SecurityRequirement(name = "basicAuth"))
    public ResponseEntity<DocumentBaseResponse> updateMetadata(String idDoc, PublicationMetadataReqDTO requestBody, HttpServletRequest request) {

        String methodName = MethodUtils.getAccessibleMethod(getClass(), "updateMetadata", String.class, PublicationMetadataReqDTO.class, HttpServletRequest.class).toString();

        log.info("Begin " + getCallingMethodName());

        try {
            String auth = request.getHeader("Authorization");

            if (!authenticate(conf, request, auth)) {
                log.info("Unauthorized : " + auth);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            var response = producer.requestBody(GatewayRoute.UPDATE_METADATA, new ParamRequest(encode(idDoc), requestBody, null), ResponseEntity.class);

            log.info("Response Status: " + response.getStatusCode());
            log.info("Response Body: " + JsonHelper.toString(response.getBody()));

            return response;


        } catch (Exception e) {
            log.error(e.getMessage());
            return GatewayRoute.getErrorResponse(e.getMessage());
        } finally {
            log.info("End " + methodName);
        }
    }

    public static boolean authenticate(Fse2BrokerProperties conf, HttpServletRequest req, String auth) {

        //logRequestParams(log,req);

        if (conf.isOauthEnabled()) {
            // TODO
        } else if (!PlainUser.authenticate(conf.getRestApplication(), req.getRemoteAddr(), auth)) {

            return false;
        }
        return true;
    }
}

