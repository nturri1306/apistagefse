package apistagefse.test;

import apistagefse.gateway.data.DTO.PublicationUpdateReqDTO;
import apistagefse.gateway.data.DTO.ValidationCreationDTO;
import apistagefse.gateway.data.enums.*;
import apistagefse.gateway.helper.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    private RestTemplate restTemplate = new RestTemplate();

    static final String userDir = System.getProperty("user.dir");

    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Test
    void create() throws IOException {

        String cdaFile = userDir + "/misc/PDF_PER_PUBBLICAZIONE/SIGNED_LAB1.pdf";

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        var fileByte = Files.readAllBytes(Paths.get(cdaFile));

        ByteArrayResource fileAsResource = new ByteArrayResource(fileByte) {
            @Override
            public String getFilename() {
                return "file";
            }
        };

        map.add("file", fileAsResource);

        ValidationCreationDTO req = new ValidationCreationDTO();

        req.setHealthDataFormat(HealthDataFormatEnum.CDA);
        req.setMode(InjectionModeEnum.ATTACHMENT);
        req.setTipologiaStruttura(HealthcareFacilityEnum.Ospedale);
        req.setAttiCliniciRegoleAccesso(new ArrayList<>());
        req.setTipoDocumentoLivAlto(TipoDocAltoLivEnum.REF);

        req.setDescriptions(new ArrayList<>());
        req.setPriorita(false);

        req.setDataInizioPrestazione(String.valueOf(new Date().getTime()));
        req.setDataFinePrestazione(String.valueOf(new Date().getTime()));

        req.setAssettoOrganizzativo(PracticeSettingCodeEnum.AD_PSC001);
        req.setIdentificativoDoc("1234567890");
        req.setIdentificativoRep("2.16.840.1.113883.2.9.2.120.4.5.1");
        req.setIdentificativoSottomissione("2.16.840.1.113883.2.9.2.120.4.3.489592");
        req.setTipoAttivitaClinica(AttivitaClinicaEnum.CON);
        req.setAdministrativeRequest("SSN");
        req.setDescriptions(new ArrayList<>());


        String urlCreation = "http://localhost:" + webServerAppCtxt.getWebServer().getPort() + "/v1/documents/validate-and-create";

        //  String urlCreation = "http://localhost:8080/v1/documents/validate-and-create";


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(req);


        map.add("requestBody", requestBodyJson);

        System.out.println(requestBodyJson);


        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        //test:test
        headers.set("Authorization", "Basic dGVzdDp0ZXN0");

        var response = restTemplate.exchange(urlCreation, HttpMethod.POST, requestEntity, ValidationCreationDTO.class);

        System.out.println(JsonHelper.toString(response.getBody(), true));

        Assert.assertTrue(response.getStatusCodeValue() == 200 || response.getStatusCodeValue() == 201);

    }


    @Test
    void update() throws IOException {

        String cdaFile = userDir + "/misc/PDF_PER_PUBBLICAZIONE/SIGNED_LAB1.pdf";

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        var fileByte = Files.readAllBytes(Paths.get(cdaFile));

        ByteArrayResource fileAsResource = new ByteArrayResource(fileByte) {
            @Override
            public String getFilename() {
                return "file";
            }
        };

        map.add("file", fileAsResource);

        PublicationUpdateReqDTO req = new PublicationUpdateReqDTO();

        req.setMode(InjectionModeEnum.ATTACHMENT);
        req.setHealthDataFormat(HealthDataFormatEnum.CDA);
        req.setTipologiaStruttura(HealthcareFacilityEnum.Ospedale);
        req.setAttiCliniciRegoleAccesso(new ArrayList<>());
        req.setTipoDocumentoLivAlto(TipoDocAltoLivEnum.REF);

        req.setDataInizioPrestazione(String.valueOf(new Date().getTime()));
        req.setDataFinePrestazione(String.valueOf(new Date().getTime()));

        req.setAssettoOrganizzativo(PracticeSettingCodeEnum.AD_PSC001);
        req.setIdentificativoDoc("1234567890");
        req.setIdentificativoRep("2.16.840.1.113883.2.9.2.120.4.5.1");
        req.setIdentificativoSottomissione("2.16.840.1.113883.2.9.2.120.4.3.489592");
        req.setTipoAttivitaClinica(AttivitaClinicaEnum.CON);


        String urlCreation = "http://localhost:" + webServerAppCtxt.getWebServer().getPort() + "/v1/documents/"+req.getIdentificativoDoc();

        //  String urlCreation = "http://localhost:8080/v1/documents/validate-and-create";


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson = objectMapper.writeValueAsString(req);


        map.add("requestBody", requestBodyJson);

        System.out.println(requestBodyJson);


        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        //test:test
        headers.set("Authorization", "Basic dGVzdDp0ZXN0");

        var response = restTemplate.exchange(urlCreation, HttpMethod.POST, requestEntity, ValidationCreationDTO.class);

        System.out.println(JsonHelper.toString(response.getBody(), true));

        Assert.assertTrue(response.getStatusCodeValue() == 200 || response.getStatusCodeValue() == 201);

    }

}
