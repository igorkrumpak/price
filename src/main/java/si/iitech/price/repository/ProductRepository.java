package si.iitech.price.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import si.iitech.price.entity.impl.EtProduct;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<EtProduct, Long> {

	public EtProduct findByUrl(String url);
}