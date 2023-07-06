package apistagefse.gateway.data.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


/**
 * The Class ErrorResponseDTO.
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class ErrorResponseDTO extends ResponseDTO {

    /**
     * Trace id log.
     */
    @Schema(description = "Indentificativo univoco della richiesta dell'utente")
    @Size(min = 0, max = 100)
    private String traceID;

    /**
     * Span id log.
     */
    @Schema(description = "Indentificativo univoco di un task della richiesta dell'utente (differisce dal traceID solo in caso di chiamate sincrone in cascata)")
    @Size(min = 0, max = 100)
    private String spanID;

    @Schema(description = "URI da utilizzare come identificativo del problema che si è verificato")
    @Size(min = 0, max = 100)
    private String type;

    @Schema(description = "Descrizione sintetica della tipologia d’errore")
    @Size(min = 0, max = 1000)
    private String title;

    @Schema(description = "Dettaglio della tipologia d’errore")
    @Size(min = 0, max = 100000)
    private String detail;

    @Schema(description = "Stato http")
    @Min(value = 100)
    @Max(value = 599)
    private Integer status;

    @Schema(description = "URI che identifica la specifica occorrenza del problema")
    @Size(min = 0, max = 100)
    private String instance;

    public ErrorResponseDTO(final LogTraceInfoDTO traceInfo, final String inType, final String inTitle, final String inDetail, final Integer inStatus, final String inInstance) {
        traceID = traceInfo.getTraceID();
        spanID = traceInfo.getSpanID();
        type = inType;
        title = inTitle;
        detail = inDetail;
        status = inStatus;
        instance = inInstance;
    }

    public ErrorResponseDTO(final LogTraceInfoDTO traceInfo) {
        traceID = traceInfo.getTraceID();
        spanID = traceInfo.getSpanID();
    }

}
