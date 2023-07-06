package apistagefse.gateway.data.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author n.turri
 */

@Getter
@Setter
public class ValidationResponse extends BaseResponse {

    private String documentResponse;
    private List<String> result = new ArrayList<>();

    public void setResultFromStringBuffer(StringBuffer result) {
        this.result = Arrays.asList(result.toString().split("\r\n"));
    }

}



