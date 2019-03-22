package si.iitech.price.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import si.iitech.price.entity.impl.EtUserProduct;

@Repository
public interface UserProductRepository extends JpaRepository<EtUserProduct, Long> {
	
}
