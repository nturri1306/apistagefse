package apistagefse.gateway.data.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LogTraceInfoDTO extends AbstractDTO {

    /**
     * Span.
     */
    private final String spanID;

    /**
     * Trace.
     */
    private final String traceID;

}
