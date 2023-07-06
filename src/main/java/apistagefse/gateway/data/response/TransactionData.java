package apistagefse.gateway.data.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


/**
 * @author n.turri
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionData extends BaseResponse {

    private String eventType;
    private String eventDate;
    private String eventStatus;
    private String message;
    private String subject;
    private String organizzazione;
    private String workflowInstanceId;
    private String traceId;
    private String issuer;
    private String expiringDate;
    private String identificativoDocumento;
    private String tipoAttivita;

}
