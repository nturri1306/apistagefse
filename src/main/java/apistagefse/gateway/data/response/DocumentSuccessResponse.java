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
public class DocumentSuccessResponse extends DocumentBaseResponse {

    private String traceID;
    private String spanID;

}
