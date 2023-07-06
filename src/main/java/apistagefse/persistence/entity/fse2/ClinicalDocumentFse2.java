package apistagefse.persistence.entity.fse2;


import apistagefse.persistence.entity.ClinicalDocumentArchitectureEntity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClinicalDocumentFse2  extends ClinicalDocumentArchitectureEntity {

    private Object body;
    private String _class;
}
