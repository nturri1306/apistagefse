package apistagefse.gateway.data.request;


import apistagefse.persistence.entity.DeleteEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author n.turri
 */

@Getter
@Setter
public class DeleteRequest extends BaseRequest<DeleteEntity> {

    @Override
    protected DeleteEntity makeEntity() {
        return new DeleteEntity();
    }

}
