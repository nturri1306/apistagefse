package apistagefse.gateway.data.request;


import apistagefse.gateway.data.HealthDataValidate;
import apistagefse.persistence.entity.ValidationEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */

@Getter
@Setter
public class ValidationRequest extends BaseRequest<ValidationEntity> {

    private String fileName;
    private HealthDataValidate requestBody;

    @Override
    protected ValidationEntity makeEntity() {
        ValidationEntity e = new ValidationEntity();
        e.setBody(requestBody);
        e.setFilename(fileName);

        return e;
    }

}
