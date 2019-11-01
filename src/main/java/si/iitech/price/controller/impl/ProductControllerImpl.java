
package si.iitech.price.controller.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import si.iitech.lib.exception.impl.UserException;
import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.service.impl.ProductService;

@Controller
@RequestMapping("/products")
public class ProductControllerImpl {

	@Autowired
	private ProductService productService;
	
	@GetMapping("prices")
	public ResponseEntity<List<EtPrice>> getPrices(@Valid @RequestBody EtProduct product) throws UserException {
		List<EtPrice> prices = productService.getPrices(product);
		return ResponseEntity.ok(prices);
	}
}
