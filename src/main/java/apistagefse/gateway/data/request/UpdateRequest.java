package apistagefse.gateway.data.request;

import apistagefse.gateway.data.DTO.PublicationUpdateReqDTO;
import apistagefse.persistence.entity.UpdateEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */

@Getter
@Setter
public class UpdateRequest extends BaseRequest<UpdateEntity> {

    private String fileName;
    private PublicationUpdateReqDTO requestBody;

    @Override
    protected UpdateEntity makeEntity() {
        UpdateEntity e = new UpdateEntity();
        e.setBody(requestBody);
        e.setFilename(fileName);

        return e;
    }

}