package apistagefse.gateway.data.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

/**
 *  Base response.
 */
@Getter
@Setter
public class ResponseDTO {

    /**
     * Trace id log.
     */
    @Size(min = 0, max = 100)
    private String traceID;

    /**
     * Span id log.
     */
    @Size(min = 0, max = 100)
    private String spanID;

    /**
     * Instantiates a new response DTO.
     */
    public ResponseDTO() {
    }

    /**
     * Instantiates a new response DTO.
     *
     * @param traceInfo the trace info
     */
    public ResponseDTO(final LogTraceInfoDTO traceInfo) {
        traceID = traceInfo.getTraceID();
        spanID = traceInfo.getSpanID();
    }

}
