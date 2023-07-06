package apistagefse.gateway.data.DTO;

import apistagefse.gateway.data.DTO.LogTraceInfoDTO;
import apistagefse.gateway.data.DTO.ResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class ResponseWifDTO extends ResponseDTO {

	@Size(min = 0, max = 10000)
	@Schema(description = "Dettaglio del warning")
	private String warning;
	
	@Size(min = 0, max = 256)
	@Schema(description = "Identificativo del workflow instance id")
    private final String workflowInstanceId;

    public ResponseWifDTO(String workflowInstanceId, LogTraceInfoDTO info, String inWarning) {
        super(info);
        this.warning = inWarning;
        this.workflowInstanceId = workflowInstanceId;
    }

}
