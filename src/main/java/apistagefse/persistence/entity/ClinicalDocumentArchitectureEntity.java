package apistagefse.persistence.entity;

import apistagefse.base.DateHelper;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import org.apache.pdfbox.signature.SignatureInfo;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author b.amoruso
 */
@Getter
@Setter
public class ClinicalDocumentArchitectureEntity extends BrokerEntity {

	private String filename;

	// Metadata extracted from CDA R2 directly
	// ClinicalDocument/recordTarget/patientRole/id@extension (id) and id@root (authority)
	private Identifier patient;
	// ClinicalDocument/author/assignedAuthor/id@extension (id) and id@root (authority), ClinicalDocument/author/time@extension (time)
	private Identifier author;
	// ClinicalDocument/id@root
	private String structureId;
	// ClinicalDocument/custodian/assignedCustodian/representedCustodianOrganization/id@extension (id) and id@root (authority)
	private Identifier custodian;
	// ClinicalDocument/legalAuthenticator/assignedEntity/id@extension (id) and id@root (authority), ClinicalDocument/legalAuthenticator/time@extension (time)
	private Identifier authenticator;

	private SignatureInfo signatureInfo;

	public void setPatient(String id, String authority) {
		patient = new Identifier(id, authority);
	}

	public void setAuthor(String id, String authority, LocalDateTime time) {
		author = new Identifier(id, authority, DateHelper.toDate(time));
	}

	public void setCustodian(String id, String authority) {
		custodian = new Identifier(id, authority);
	}

	public void setAuthenticator(String id, String authority, LocalDateTime time) {
		authenticator = new Identifier(id, authority, DateHelper.toDate(time));
	}

	public SignatureInfo getSignatureInfo() {
		return signatureInfo;
	}

	public void setSignatureInfo(SignatureInfo signatureInfo) {
		this.signatureInfo = signatureInfo;
	}



	private String cda_xml;
	private String cda_json;


	private Date data_inizio_validita;
	private Date data_fine_validita;


	@Getter
	@Setter
	public static class Identifier {

		@Indexed
		private String id;
		@Indexed
		private String authority;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
		private Date time;

		public Identifier(String id, String authority) {
			this.id = id;
			this.authority = authority;
		}

		public Identifier(String id, String authority, Date time) {
			this(id, authority);

			this.time = time;
		}

	}

}
