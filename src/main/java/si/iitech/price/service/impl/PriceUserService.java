package si.iitech.price.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.entity.impl.EtUserProduct;
import si.iitech.price.repository.PriceUserRepository;
import si.iitech.price.repository.UserProductRepository;

@Service
public class PriceUserService {

	@Autowired
	private PriceUserRepository userRepository;

	@Autowired
	private UserProductRepository userProductRepository;
	
	public EtPriceUser findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void addProductToUser(EtProduct product, EtPriceUser user) {
		EtUserProduct userProduct = new EtUserProduct(user, product);
		userProductRepository.save(userProduct);
	}
	
//	public void checkIfUserHasThisProduct()
}
