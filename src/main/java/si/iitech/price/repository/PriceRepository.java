package si.iitech.price.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtProduct;

@Repository
public interface PriceRepository extends CrudRepository<EtPrice, Long> {

	public EtPrice findFirstByProductOrderByPriceDateDesc(EtProduct product);

	public List<EtPrice> findByProductOrderByPriceDateDesc(EtProduct existingProduct);
}
