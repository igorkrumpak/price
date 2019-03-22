package si.iitech.price.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import si.iitech.lib.exception.impl.WebParserException;
import si.iitech.lib.util.DateUtils;
import si.iitech.price.definition.impl.MindfactoryDefinition;
import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtPriceSource;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.impl.PriceException;
import si.iitech.price.repository.PriceSourceRepository;
import si.iitech.price.service.impl.ProductService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MindfactoryTest {

	@Autowired
	private PriceSourceRepository priceSourceRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("priceBatch")
	private Job job;

	@Test
	public void testAddProductsAndPrices() throws WebParserException, PriceException {
		addProductAndPrice(MindfactoryProduct.PRODUCT_1_URL);
		addProductAndPrice(MindfactoryProduct.PRODUCT_2_URL);
		addProductAndPrice(MindfactoryProduct.PRODUCT_3_URL);
		addProductAndPrice(MindfactoryProduct.PRODUCT_4_URL);
	}

	@Test
	public void testPriceBatch() throws WebParserException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException, PriceException {
		EtPriceSource mindfactoryPriceSource = priceSourceRepository.findByTitle(MindfactoryDefinition.TITLE);
		assertNotNull(mindfactoryPriceSource);
		EtProduct product1 = addProduct(MindfactoryProduct.PRODUCT_1_URL);
		EtProduct product2 = addProduct(MindfactoryProduct.PRODUCT_2_URL);
		EtProduct product3 = addProduct(MindfactoryProduct.PRODUCT_3_URL);
		EtProduct product4 = addProduct(MindfactoryProduct.PRODUCT_4_URL);
		JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		while(jobExecution.isRunning()) {
			assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		}
		assertNotNull(productService.getLatestPrice(product1.getOid()));
		assertNotNull(productService.getLatestPrice(product2.getOid()));
		assertNotNull(productService.getLatestPrice(product3.getOid()));
		assertNotNull(productService.getLatestPrice(product4.getOid()));
	}
	
	@Test
	public void testGetProductSource() throws PriceException {
		EtPriceSource productSource = productService.getPriceSourceFromProductUrl(MindfactoryProduct.PRODUCT_1_URL);
		assertNotNull(productSource);
		assertEquals(MindfactoryDefinition.URL, productSource.getUrl());
	}
	
	@Test
	public void testGetProductTitle() throws PriceException, WebParserException {
		assertNotNull(productService.parseProductTitle(MindfactoryProduct.PRODUCT_1_URL));
	}

	private EtProduct addProduct(String url) throws WebParserException, PriceException {
		EtProduct product = new EtProduct();
		product.setUrl(url);
		EtProduct newProduct = productService.newProduct(product);
		assertNotNull(newProduct.getTitle());
		return newProduct;
	}

	private void addProductAndPrice(String url) throws WebParserException, PriceException {
		EtProduct product = addProduct(url);

		EtPrice price = new EtPrice();
		price.setPriceDate(DateUtils.getNow());

		Double productPrice = productService.parseProductPrice(url);

		price.setPrice(productPrice);
		productService.addPrice(product.getOid(), price);
	}
}
