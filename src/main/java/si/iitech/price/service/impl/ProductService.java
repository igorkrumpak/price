package si.iitech.price.service.impl;

import java.util.Optional;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import si.iitech.exception.impl.WebParserException;
import si.iitech.price.entities.impl.EtPrice;
import si.iitech.price.entities.impl.EtPriceSource;
import si.iitech.price.entities.impl.EtProduct;
import si.iitech.price.exception.impl.PriceRuntimeException;
import si.iitech.price.repository.PriceRepository;
import si.iitech.price.repository.PriceSourceRepository;
import si.iitech.price.repository.ProductRepository;
import si.iitech.util.WebParser;

@Service
public class ProductService {

	@Autowired
	private PriceSourceRepository priceSourceRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PriceRepository priceRepository;

	public String parseProductTitle(Long sourceOid, String url) throws WebParserException {
		EtPriceSource priceSource = getPriceSource(sourceOid);
		Document document = WebParser.createInstance().readWebSite(url);
		return priceSource.getParserClassInstance().getTitle(document);
	}

	public Double parseProductPrice(Long sourceOid, String url) throws WebParserException {
		EtPriceSource priceSource = getPriceSource(sourceOid);
		Document document = WebParser.createInstance().readWebSite(url);
		return priceSource.getParserClassInstance().getPrice(document);
	}

	@Transactional
	public EtProduct saveProduct(Long sourceOid, EtProduct product) {
		EtProduct existingProduct = productRepository.findByUrl(product.getUrl());
		if (existingProduct != null)
			return existingProduct;
		EtPriceSource priceSource = getPriceSource(sourceOid);
		priceSource.addProduct(product);
		return productRepository.save(product);
	}

	@Transactional
	public void addPrice(Long productOid, EtPrice price) {
		EtProduct product = getProduct(productOid);
		product.addPrice(price);
		priceRepository.save(price);
	}

	public EtPrice getLatestPrice(Long productOid) {
		getProduct(productOid);
		return priceRepository.findFirstByProductOidOrderByPriceDateDesc(productOid);
	}

	private EtPriceSource getPriceSource(Long sourceOid) {
		Optional<EtPriceSource> priceSourceOptional = priceSourceRepository.findById(sourceOid);
		if (!priceSourceOptional.isPresent())
			throw new PriceRuntimeException("Price source is not found!");
		EtPriceSource priceSource = priceSourceOptional.get();
		return priceSource;
	}

	private EtProduct getProduct(Long productOid) {
		Optional<EtProduct> productOptional = productRepository.findById(productOid);
		if (!productOptional.isPresent())
			throw new PriceRuntimeException("Product is not found!");
		EtProduct product = productOptional.get();
		return product;
	}

}
