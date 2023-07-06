package apistagefse.gateway.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse extends DocumentBaseResponse {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
