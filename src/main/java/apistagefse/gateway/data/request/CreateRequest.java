package apistagefse.gateway.data.request;


import apistagefse.gateway.data.DTO.ValidationCreationDTO;
import apistagefse.persistence.entity.CreateEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */

@Getter
@Setter
public class CreateRequest extends BaseRequest<CreateEntity> {

    private String fileName;
    private ValidationCreationDTO requestBody;

    @Override
    protected CreateEntity makeEntity() {
        CreateEntity e = new CreateEntity();
        e.setBody(requestBody);
        e.setFilename(fileName);

        return e;
    }

}
