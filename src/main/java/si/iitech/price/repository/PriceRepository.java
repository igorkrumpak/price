package si.iitech.price.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import si.iitech.price.entities.impl.EtPrice;

@Repository
public interface PriceRepository extends CrudRepository<EtPrice, Long> {

	public EtPrice findFirstByProductOidOrderByPriceDateDesc(Long productOid);
}
