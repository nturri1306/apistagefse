package apistagefse.persistence;


import apistagefse.persistence.entity.BrokerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author b.amoruso
 */
@NoRepositoryBean
public interface DaoFacade <T extends BrokerEntity> extends MongoRepository<T, String> {

}
