package apistagefse.gateway.data.request;


import apistagefse.gateway.data.DTO.PublicationMetadataReqDTO;
import apistagefse.persistence.entity.MetadataEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */

@Getter
@Setter
public class MetadataRequest extends BaseRequest<MetadataEntity> {

    private PublicationMetadataReqDTO requestBody;

    @Override
    protected MetadataEntity makeEntity() {
        MetadataEntity e = new MetadataEntity();
         e.setBody(requestBody);

        return e;
    }

}
