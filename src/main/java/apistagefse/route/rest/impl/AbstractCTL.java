package apistagefse.route.rest.impl;

import apistagefse.gateway.data.DTO.ErrorResponseDTO;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AbstractCTL {



    protected String getCallingMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 3) {
            return stackTrace[2].getMethodName();
        }
        return null;
    }

    public String getMethodName() {
        Method currentMethod = new Object() {
        }.getClass().getEnclosingMethod();
        return currentMethod.getName();
    }

    public String encode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }
}
