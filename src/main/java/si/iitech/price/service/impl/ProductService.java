package si.iitech.price.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import si.iitech.lib.exception.impl.WebParserException;
import si.iitech.lib.util.WebParser;
import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtPriceSource;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.impl.PriceException;
import si.iitech.price.exception.impl.PriceRuntimeException;
import si.iitech.price.repository.PriceRepository;
import si.iitech.price.repository.PriceSourceRepository;
import si.iitech.price.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private PriceSourceRepository priceSourceRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PriceRepository priceRepository;

	public String parseProductTitle(String url) throws WebParserException, PriceException {
		EtPriceSource priceSource = getPriceSourceFromProductUrl(url);
		Document document = WebParser.createInstance().readWebSite(url);
		return priceSource.getParserClassInstance().getTitle(document);
	}

	public Double parseProductPrice(String url) throws WebParserException, PriceException {
		EtPriceSource priceSource = getPriceSourceFromProductUrl(url);
		Document document = WebParser.createInstance().readWebSite(url);
		return priceSource.getParserClassInstance().getPrice(document);
	}
	
	@Transactional
	public EtProduct newProduct(EtProduct product) throws PriceException, WebParserException {
		EtProduct existingProduct = productRepository.findByUrl(product.getUrl());
		if (existingProduct != null)
			return existingProduct;
		EtPriceSource priceSource = getPriceSourceFromProductUrl(product.getUrl());
		String title = parseProductTitle(product.getUrl());
		product.setTitle(title);
		product.setPriceSource(priceSource);
		return productRepository.save(product);
	}

	public EtPriceSource getPriceSourceFromProductUrl(String productUrl) throws PriceException {
		URL url = null;
		try {
			url = new URL(productUrl);
		} catch (MalformedURLException e) {
			throw new PriceException("Invalid product url!");
		}
		EtPriceSource priceSource = priceSourceRepository.findByUrl(url.getHost());
		if(priceSource == null) {
			throw new PriceException("Source not found!");
		}
		return priceSource;
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

	private EtProduct getProduct(Long productOid) {
		Optional<EtProduct> productOptional = productRepository.findById(productOid);
		if (!productOptional.isPresent())
			throw new PriceRuntimeException("Product is not found!");
		EtProduct product = productOptional.get();
		return product;
	}
}
