package si.iitech.price.batch.impl;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import si.iitech.lib.util.DateUtils;
import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.service.impl.ProductService;

public class PriceItemProcesor implements ItemProcessor<EtProduct, EtPrice> {

	@Autowired
	private ProductService productService;

	@Override
	public EtPrice process(EtProduct product) throws Exception {
		Double productPrice = productService.parseProductPrice(product.getUrl());
		EtPrice price = new EtPrice();
		price.setPrice(productPrice);
		price.setPriceDate(DateUtils.getNow());
		product.addPrice(price);
		return price;
	}

}
