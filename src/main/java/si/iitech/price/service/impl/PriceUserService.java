package si.iitech.price.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import si.iitech.lib.exception.impl.WebParserException;
import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.entity.impl.EtSelectedProduct;
import si.iitech.price.exception.PriceExceptionMessages;
import si.iitech.price.exception.impl.PriceException;
import si.iitech.price.repository.PriceUserRepository;
import si.iitech.price.repository.UserProductRepository;

@Service
public class PriceUserService {

	@Autowired
	private PriceUserRepository userRepository;

	@Autowired
	private UserProductRepository userProductRepository;

	@Autowired
	private ProductService productService;

	public EtPriceUser findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	private void addProductToUser(EtProduct product, EtPriceUser user) {
		EtSelectedProduct userProduct = new EtSelectedProduct(user, product);
		userProductRepository.save(userProduct);
	}

	private boolean doesUserHasThisProduct(EtPriceUser user, EtProduct product) {
		EtProduct existingProduct = productService.findByUrl(product.getUrl());
		if (existingProduct == null) return false;
		return userProductRepository.existsByUserAndProduct(user, existingProduct);
	}

	public List<EtProduct> getProductsFromUser(EtPriceUser user) {
		return userProductRepository.getProductByUser(user);
	}

	public EtProduct addProduct(EtPriceUser user, EtProduct product) throws PriceException, WebParserException {
		if(doesUserHasThisProduct(user, product)) throw new PriceException(PriceExceptionMessages.USER_ALREADY_HAS_THIS_PRODUCT);
		product = productService.newProduct(product);
		addProductToUser(product, user);
		return product;
	}
}
