package apistagefse.gateway.data.request;


import apistagefse.persistence.entity.TraceIdStatusEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */

@Getter
@Setter
public class TraceIdStatusRequest extends BaseRequest<TraceIdStatusEntity> {

    @Override
    protected TraceIdStatusEntity makeEntity() {
        return new TraceIdStatusEntity();
    }

}