package apistagefse.persistence.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author b.amoruso
 */
@Getter
@Setter
@Document(collection = "fse2_traceidstatus")
public class TraceIdStatusEntity extends BrokerEntity {
}
