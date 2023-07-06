package apistagefse.gateway.data.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author n.turri
 */

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDataResponse extends BaseResponse {

    private String spanID;
    private String traceID;

    List<TransactionData> transactionData = new ArrayList<TransactionData>();

}
