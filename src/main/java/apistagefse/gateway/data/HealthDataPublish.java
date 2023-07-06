package apistagefse.gateway.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthDataPublish extends HealthDataUpdate {

    private String workflowInstanceId;
    private boolean priorita;

}
