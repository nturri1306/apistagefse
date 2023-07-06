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
public class DocumentErrorResponse extends DocumentBaseResponse {

    private String traceID;
    private String spanID;
    private String type;
    private String title;
    private String detail;
    private int status;
    private String instance;
    private String govway_id;

}