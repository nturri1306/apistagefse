package apistagefse.gateway.data.request;

import apistagefse.persistence.entity.WorkflowInstanceIdStatusEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkflowInstanceIdStatusRequest extends BaseRequest<WorkflowInstanceIdStatusEntity> {

    @Override
    protected WorkflowInstanceIdStatusEntity makeEntity() {
        return new WorkflowInstanceIdStatusEntity();
    }

}
