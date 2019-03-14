package si.iitech.price.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import si.iitech.price.entities.impl.EtPriceSource;

@Repository
public interface PriceSourceRepository extends CrudRepository<EtPriceSource, Long> {
	
	public boolean existsByUrl(String url);
	
	public EtPriceSource findByTitle(String title);

}
