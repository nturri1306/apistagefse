package apistagefse.gateway.data.response;


import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */
@Getter
@Setter
public class DocumentResponse extends DocumentBaseResponse {

    private String eventType;
    private String eventDate;
    private String eventStatus;
    private String subject;
    private String organizzazione;
    private String issuer;
    private String expiringDate;
    private String identificativoDocumento;
    private String tipoAttivita;
}
