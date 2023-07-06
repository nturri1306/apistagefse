package apistagefse.persistence.entity;

import apistagefse.gateway.data.DTO.PublicationResDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author b.amoruso
 */
@Getter
@Setter
@Document(collection = "fse2_publish")
public class PublishEntity extends ClinicalDocumentArchitectureEntity {

    private PublicationResDTO body;

}
