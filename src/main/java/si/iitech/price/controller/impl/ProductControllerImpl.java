
package si.iitech.price.controller.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import si.iitech.lib.exception.impl.WebParserException;
import si.iitech.lib.security.impl.SecurityConstants;
import si.iitech.lib.util.JWTUtils;
import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.impl.PriceException;
import si.iitech.price.service.impl.PriceUserService;
import si.iitech.price.service.impl.ProductService;

@Controller
@RequestMapping("/products")
public class ProductControllerImpl {

	@Autowired
	private ProductService productService;

	@Autowired
	private PriceUserService userService;

	@PostMapping
	public ResponseEntity<EtProduct> newProduct(
			@RequestHeader(value = SecurityConstants.TOKEN) String token,
			@Valid @RequestBody EtProduct product) throws PriceException, WebParserException {
		EtPriceUser user = userService.findByEmail(JWTUtils.getUser(token));
		
		EtProduct createdProduct = productService.newProduct(product);
		
		userService.addProductToUser(createdProduct, user);
		return ResponseEntity.ok(createdProduct);
	}

}
