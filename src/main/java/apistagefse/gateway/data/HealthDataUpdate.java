package apistagefse.gateway.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthDataUpdate {

    private String healthDataFormat;
    private String mode;
    private String tipologiaStruttura;
    private List<String> attiCliniciRegoleAccesso;
    private String identificativoDoc;
    private String identificativoRep;
    private String tipoDocumentoLivAlto;
    private String assettoOrganizzativo;
    private String dataInizioPrestazione;
    private String dataFinePrestazione;
    private String conservazioneANorma;
    private String tipoAttivitaClinica;
    private String identificativoSottomissione;
    private List<String> descriptions;
    private String administrativeRequest;

}
