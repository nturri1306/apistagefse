package apistagefse.persistence.entity;


import apistagefse.gateway.data.HealthDataValidate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author b.amoruso
 */
@Getter
@Setter
@Document(collection = "fse2_validation")
public class ValidationEntity extends ClinicalDocumentArchitectureEntity {

    private HealthDataValidate body;

}
