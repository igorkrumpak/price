package si.iitech.price.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import si.iitech.price.entity.impl.EtProduct;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<EtProduct, Long> {

	@EntityGraph(attributePaths = "prices")
	public EtProduct findByUrl(String url);
	
}