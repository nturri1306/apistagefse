package apistagefse.gateway.data.request;



import apistagefse.persistence.entity.BrokerEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


/**
 * @author n.turri
 */

@Getter
@Setter
@JsonIgnoreProperties(value = {"requestDataTime"}, allowGetters = true)
public abstract class BaseRequest <T extends BrokerEntity> {

    private String id;
    private Date requestDataTime;
    private String bearerToken;
    private String claimsToken;
    private String serviceUrl;

    public BaseRequest() {
        requestDataTime = new Date();
    }

    public T toEntity() {
        T t = makeEntity();
        t.setRequestId(id);
        t.setRequested(requestDataTime);
        t.setBearerToken(bearerToken);
        t.setClaimsToken(claimsToken);
        t.setServiceUrl(serviceUrl);

        return t;
    }

    protected abstract T makeEntity();

}
