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

import si.iitech.exception.impl.WebParserException;
import si.iitech.price.definition.impl.MindfactoryDefinition;
import si.iitech.price.entities.impl.EtPrice;
import si.iitech.price.entities.impl.EtPriceSource;
import si.iitech.price.entities.impl.EtProduct;
import si.iitech.price.repository.PriceSourceRepository;
import si.iitech.price.service.impl.ProductService;
import si.iitech.util.DateUtils;

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
	public void testAddProductsAndPrices() throws WebParserException {
		EtPriceSource mindfactoryPriceSource = priceSourceRepository.findByTitle(MindfactoryDefinition.TITLE);
		assertNotNull(mindfactoryPriceSource);

		addProductAndPrice(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/Thermalright-ARO-M14-AMD-Ryzen-CPU-Kuehler-grau-Tower-Kuehler_1238354.html");
		addProductAndPrice(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/6GB-KFA2-GeForce-GTX-1060-OC-Aktiv-PCIe-3-0-x16--Retail-_1286394.html");
		addProductAndPrice(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/AMD-Ryzen-5-2600X-6x-3-60GHz-So-AM4-BOX_1233731.html");
		addProductAndPrice(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/Notebook-17-3Zoll--43-94cm--Lenovo-V320-17IKB-i5-8250U-8-256SSD-FHD-mat_1276867.html");
	}

	@Test
	public void testPriceBatch() throws WebParserException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		EtPriceSource mindfactoryPriceSource = priceSourceRepository.findByTitle(MindfactoryDefinition.TITLE);
		assertNotNull(mindfactoryPriceSource);
		EtProduct product1 = addProduct(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/Thermalright-ARO-M14-AMD-Ryzen-CPU-Kuehler-grau-Tower-Kuehler_1238354.html");
		addProduct(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/6GB-KFA2-GeForce-GTX-1060-OC-Aktiv-PCIe-3-0-x16--Retail-_1286394.html");
		addProduct(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/AMD-Ryzen-5-2600X-6x-3-60GHz-So-AM4-BOX_1233731.html");
		addProduct(mindfactoryPriceSource,
				"https://www.mindfactory.de/product_info.php/Notebook-17-3Zoll--43-94cm--Lenovo-V320-17IKB-i5-8250U-8-256SSD-FHD-mat_1276867.html");
		JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		while(jobExecution.isRunning()) {
			assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		}
		productService.getLatestPrice(product1.getOid());
	}

	private EtProduct addProduct(EtPriceSource priceSource, String url) throws WebParserException {
		String productTitle = productService.parseProductTitle(priceSource.getOid(), url);
		assertNotNull(productTitle);

		EtProduct product = new EtProduct();
		product.setTitle(productTitle);
		product.setUrl(url);
		return productService.saveProduct(priceSource.getOid(), product);
	}

	private void addProductAndPrice(EtPriceSource priceSource, String url) throws WebParserException {
		EtProduct product = addProduct(priceSource, url);

		EtPrice price = new EtPrice();
		price.setPriceDate(DateUtils.getNow());

		Double productPrice = productService.parseProductPrice(priceSource.getOid(), url);
		assertNotNull(productPrice);

		price.setPrice(productPrice);
		productService.addPrice(product.getOid(), price);
	}
}
