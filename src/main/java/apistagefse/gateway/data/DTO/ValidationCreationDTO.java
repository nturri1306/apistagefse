package apistagefse.gateway.data.DTO;

import apistagefse.gateway.data.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationCreationDTO extends  AbstractDTO {

    @Schema(description = "Formato dei dati sanitari")
    private HealthDataFormatEnum healthDataFormat;

    @Schema(description = "Modalità di iniezione del CDA")
    private InjectionModeEnum mode;

    @Schema(description = "Tipologia struttura che ha prodotto il documento", required = true)
    private HealthcareFacilityEnum tipologiaStruttura;

    private List<String> attiCliniciRegoleAccesso;
    private String identificativoDoc;
    private String identificativoRep;

    @Schema(description = "Tipo documento alto livello", required = true)
    private TipoDocAltoLivEnum tipoDocumentoLivAlto;


    @Schema(description = "Assetto organizzativo che ha portato alla creazione del documento", required = true)
    private PracticeSettingCodeEnum assettoOrganizzativo;

    private String dataInizioPrestazione;
    private String dataFinePrestazione;

    @Schema(description = "Tipo attività clinica",required = true)
    private AttivitaClinicaEnum tipoAttivitaClinica;

    private String identificativoSottomissione;
    private boolean priorita;
    private List<String> descriptions;
    private String administrativeRequest;
}
