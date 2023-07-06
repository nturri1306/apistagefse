package apistagefse.gateway.imported.dispatcher.enums;

import lombok.Getter;

public enum InjectionModeEnum {

    ATTACHMENT("A"),
    RESOURCE("R");

    @Getter
    private String code;

    private InjectionModeEnum(String inCode) {
        code = inCode;
    }
}
