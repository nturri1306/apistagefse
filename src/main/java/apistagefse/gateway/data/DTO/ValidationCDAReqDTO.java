package apistagefse.gateway.data.DTO;

import apistagefse.gateway.data.enums.ActivityEnum;
import apistagefse.gateway.data.enums.HealthDataFormatEnum;
import apistagefse.gateway.data.enums.InjectionModeEnum;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

/**
 *	Request body validazione CDA.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
@JacksonStdImpl
public class ValidationCDAReqDTO extends AbstractDTO {

    @Schema(description = "Formato dei dati sanitari")
    private HealthDataFormatEnum healthDataFormat;

    @Schema(description = "Modalità di iniezione del CDA")
    private InjectionModeEnum mode;

    @Schema(description = "Attività del gateway", required = true)
    private ActivityEnum activity;

}
