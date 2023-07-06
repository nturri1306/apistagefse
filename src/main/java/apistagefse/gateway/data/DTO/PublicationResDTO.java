package apistagefse.gateway.data.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationResDTO extends ResponseDTO {

    @Size(min = 0, max = 10000)
    @Schema(description = "Dettaglio del warning")
    private String warning;

    @Size(min = 0, max = 256)
    @Schema(description = "Identificativo del workflow instance id")
    private String workflowInstanceId;

    public PublicationResDTO(final LogTraceInfoDTO traceInfo, String inWarning, final String inWorkflowInstanceId) {
        super(traceInfo);
        warning = inWarning;
        workflowInstanceId = inWorkflowInstanceId;
    }
}

