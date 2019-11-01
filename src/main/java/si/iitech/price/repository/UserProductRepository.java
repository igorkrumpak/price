package si.iitech.price.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.entity.impl.EtSelectedProduct;

@Repository
public interface UserProductRepository extends JpaRepository<EtSelectedProduct, Long> {

	public boolean existsByUserAndProduct(EtPriceUser user, EtProduct existingProduct);

	public List<EtSelectedProduct> getProdutsByUser(EtPriceUser user);

	@Query(value = 
			"select " +
			"selectedProduct.product " +
			"from si.iitech.price.entity.impl.EtSelectedProduct selectedProduct " +
			"left join fetch selectedProduct.product.prices prices " +
			"where selectedProduct.user = ?1 " +
			"and prices.priceDate = (select max(p.priceDate) from si.iitech.price.entity.impl.EtPrice p where p.product.oid = selectedProduct.product.oid)")
	public List<EtProduct> getProductByUser(EtPriceUser user);

}
