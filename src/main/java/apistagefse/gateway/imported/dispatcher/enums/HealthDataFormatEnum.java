package apistagefse.gateway.imported.dispatcher.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum HealthDataFormatEnum {

    CDA("V");

    @Getter
    private String code;

    private HealthDataFormatEnum(String inCode) {
        code = inCode;
    }

}
