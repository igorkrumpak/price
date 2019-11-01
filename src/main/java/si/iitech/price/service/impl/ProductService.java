package si.iitech.price.service.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import si.iitech.lib.exception.impl.WebParserException;
import si.iitech.lib.util.DateUtils;
import si.iitech.lib.util.WebParser;
import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtPriceSource;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.PriceExceptionMessages;
import si.iitech.price.exception.impl.PriceException;
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
	
	public EtPriceSource getPriceSourceFromProductUrl(String productUrl) throws PriceException {
		URL url = null;
		try {
			url = new URL(productUrl);
		} catch (MalformedURLException e) {
			throw new PriceException(PriceExceptionMessages.INVALID_PRODUCT_URL);
		}
		EtPriceSource priceSource = priceSourceRepository.findByUrl(url.getHost());
		if(priceSource == null) {
			throw new PriceException(PriceExceptionMessages.SOURCE_NOT_FOUND);
		}
		return priceSource;
	}
	
	@Transactional
	public EtProduct newProduct(EtProduct product) throws PriceException, WebParserException {
		EtProduct existingProduct = findByUrl(product.getUrl());
		if (existingProduct != null)
			return existingProduct;
		EtPriceSource priceSource = getPriceSourceFromProductUrl(product.getUrl());
		String title = parseProductTitle(product.getUrl());
		product.setTitle(title);
		product.setPriceSource(priceSource);
		EtProduct savedProduct = productRepository.save(product);
		newPrice(savedProduct);
		return savedProduct;
	}

	@Transactional
	public EtPrice newPrice(EtProduct product) throws WebParserException, PriceException {
		EtPrice price = new EtPrice();
		price.setPrice(parseProductPrice(product.getUrl()));
		price.setPriceDate(DateUtils.getNow());
		product.addPrice(price);
		return priceRepository.save(price);
	}

	public List<EtPrice> getPrices(EtProduct product) {
		EtProduct existingProduct = findByUrl(product.getUrl());
		return priceRepository.findByProductOrderByPriceDateDesc(existingProduct);
	}
	
	public EtPrice getLatestPrice(EtProduct product) {
		return priceRepository.findFirstByProductOrderByPriceDateDesc(product);
	}

	public EtProduct findByUrl(String url) {
		return productRepository.findByUrl(url);
	}
}
