package si.iitech.price.batch.impl;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import si.iitech.price.entities.impl.EtPrice;
import si.iitech.price.entities.impl.EtPriceSource;
import si.iitech.price.entities.impl.EtProduct;
import si.iitech.price.service.impl.ProductService;
import si.iitech.util.DateUtils;

public class PriceItemProcesor implements ItemProcessor<EtProduct, EtPrice> {

	@Autowired
	private ProductService productService;

	@Override
	public EtPrice process(EtProduct product) throws Exception {
		EtPriceSource sourceOid = product.getPriceSource();
		Double productPrice = productService.parseProductPrice(sourceOid.getOid(), product.getUrl());
		EtPrice price = new EtPrice();
		price.setPrice(productPrice);
		price.setPriceDate(DateUtils.getNow());
		product.addPrice(price);
		return price;
	}

}
