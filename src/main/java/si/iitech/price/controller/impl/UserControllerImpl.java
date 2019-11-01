package si.iitech.price.controller.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import si.iitech.lib.controller.UserController;
import si.iitech.lib.exception.impl.UserException;
import si.iitech.lib.exception.impl.WebParserException;
import si.iitech.lib.security.impl.SecurityConstants;
import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.impl.PriceException;
import si.iitech.price.service.impl.PriceUserService;


@Controller
@RequestMapping("/users")
public class UserControllerImpl extends UserController<EtPriceUser> {
	
	@Autowired
	private PriceUserService userService;

	public UserControllerImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
		super(bCryptPasswordEncoder);
	}

	@PostMapping("products")
	public ResponseEntity<EtProduct> newProduct(
			@RequestHeader(value = SecurityConstants.TOKEN) String token,
			@Valid @RequestBody EtProduct product) throws PriceException, WebParserException, UserException {
		EtPriceUser user = getUser(token);
		EtProduct createdProduct = userService.addProduct(user, product);
		return ResponseEntity.ok(createdProduct);
	}
	
	@GetMapping("products")
	public ResponseEntity<List<EtProduct>> getProducts(
			@RequestHeader(value = SecurityConstants.TOKEN) String token) throws UserException {
		EtPriceUser user = getUser(token);
		List<EtProduct> products = userService.getProductsFromUser(user);
		return ResponseEntity.ok(products);
	}

}
