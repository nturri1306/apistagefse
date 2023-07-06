package apistagefse.persistence.entity;

import apistagefse.gateway.data.DTO.PublicationMetadataReqDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author b.amoruso
 */
@Getter
@Setter
@Document(collection = "fse2_metadata")
public class MetadataEntity extends BrokerEntity {

    private PublicationMetadataReqDTO body;

}
