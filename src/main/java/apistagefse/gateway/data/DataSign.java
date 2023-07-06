package apistagefse.gateway.data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataSign {

    private String alg;
    private String typ;
    private String sub;
    private String subject_role;
    private String purpose_of_use;
    private String iss;
    private String subject_application_id;
    private String subject_application_vendor;
    private String subject_application_version;
    private String locality;
    private String subject_organization_id;
    private String subject_organization;
    private String aud;
    private boolean patient_consent;
    private String action_id;
    private String resource_hl7_type;
    private String jti;
    private String person_id;
    private String pem_path;
    private String p12_path;

}
