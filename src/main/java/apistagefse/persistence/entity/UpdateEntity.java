package apistagefse.persistence.entity;

import apistagefse.gateway.data.DTO.PublicationUpdateReqDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author b.amoruso
 */
@Getter
@Setter
@Document(collection = "fse2_update")
public class UpdateEntity extends ClinicalDocumentArchitectureEntity {

    private PublicationUpdateReqDTO body;

}
