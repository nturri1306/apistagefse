package apistagefse.persistence.entity;

import apistagefse.gateway.data.DTO.ValidationCreationDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author b.amoruso
 */
@Getter
@Setter
@Document(collection = "fse2_create")
public class CreateEntity extends ClinicalDocumentArchitectureEntity {

    private ValidationCreationDTO body;

}
