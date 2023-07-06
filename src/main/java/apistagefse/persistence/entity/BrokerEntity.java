package apistagefse.persistence.entity;

import apistagefse.gateway.data.response.BaseResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

/**
 * @author b.amoruso
 */
@Getter
@Setter
public class BrokerEntity {

	@Id
	private String id;
	private String previousId;
	@Indexed
	private String requestId;
	@Indexed
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date created;
	@Indexed
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date requested;
	@Indexed
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
	private Date sent;

	private String bearerToken;
	private String claimsToken;
	private String serviceUrl;

	private BaseResponse response;

	@Indexed
	private boolean removed;

	public BrokerEntity() {
		created = new Date();
	}

	public void asSent() {
		sent = new Date();
	}

	@Override
    public boolean equals(Object object) {
        if (object instanceof BrokerEntity)
        	return ((BrokerEntity)object).getId().equals(getId());

        return false;
    }

	@Override
    public int hashCode() {
		return getId() == null ? super.hashCode() : getId().hashCode();
    }

	@Override
	public String toString() {
		return String.format("Entity[id=%s, created=%s, requested=%s, sent=%s, removed=%b]",
			id, created, requested, sent, removed);
	}

}
