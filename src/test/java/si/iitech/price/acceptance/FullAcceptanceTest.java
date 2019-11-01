package si.iitech.price.acceptance;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import si.iitech.lib.security.impl.SecurityConstants;
import si.iitech.lib.util.StringUtils;
import si.iitech.price.PriceTest;
import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.PriceExceptionMessages;
import si.iitech.price.exception.impl.RestException;
import si.iitech.price.service.impl.PriceUserService;
import si.iitech.price.service.impl.ProductService;

public class FullAcceptanceTest extends PriceTest {

	@Autowired
	private PriceUserService userService;
	
	@Autowired
	private ProductService productService;
	
	private String token;
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("priceBatch")
	private Job job;
	
	@Autowired
	private MockMvc mvc;

	@Test
	public void acceptanceTest() throws Exception {
		String email = getNextCountString() + "test@gmail.com";
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/register")
					.content(StringUtils.asJsonString(new EtPriceUser(email, "test1234")))
					.contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
				    .andExpect(status().isOk());
		EtPriceUser user = userService.findByEmail(email);
		token = this.mvc.perform(
				MockMvcRequestBuilders
					.post("/login")
					.content(StringUtils.asJsonString(new EtPriceUser(email, "test1234")))
					.contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
				    .andExpect(status().isOk()).andReturn().getResponse().getHeader(SecurityConstants.TOKEN);
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/products")
					.header(SecurityConstants.TOKEN, token)
					.content(StringUtils.asJsonString(new EtProduct(null, "https://www.broken.de/product_info.php/6GB-KFA2-GeForce-GTX-1060-OC-Aktiv-PCIe-3-0-x16--Retail-_1286394.html")))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().string(StringUtils.asJsonString(new RestException(PriceExceptionMessages.SOURCE_NOT_FOUND))));
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/products")
					.header(SecurityConstants.TOKEN, token)
					.content(StringUtils.asJsonString(new EtProduct(null, "kende/product_info.php/6GB-KFA2-GeForce-GTX-1060-OC-Aktiv-PCIe-3-0-x16--Retail-_1286394.html")))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().string(StringUtils.asJsonString(new RestException(PriceExceptionMessages.INVALID_PRODUCT_URL))));
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/products")
					.header(SecurityConstants.TOKEN, token)
					.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_3_URL)))
					.contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
				    .andExpect(status().isOk());
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/products")
					.header(SecurityConstants.TOKEN, token)
					.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_2_URL)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk());
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/products")
					.header(SecurityConstants.TOKEN, token)
					.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_2_URL)))
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(MockMvcResultMatchers.content().string(StringUtils.asJsonString(new RestException(PriceExceptionMessages.USER_ALREADY_HAS_THIS_PRODUCT))));
		
		JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		while(jobExecution.isRunning()) {
			assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
		}
		
		List<EtProduct> products = userService.getProductsFromUser(user);
		this.mvc.perform(
				MockMvcRequestBuilders
					.get("/users/products")
					.header(SecurityConstants.TOKEN, token)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(
							StringUtils.asJsonString(
								products
							)
					));
		List<EtPrice> prices = productService.getPrices(new EtProduct(null, MindfactoryProduct.PRODUCT_2_URL));
		this.mvc.perform(
				MockMvcRequestBuilders
					.get("/products/prices")
					.header(SecurityConstants.TOKEN, token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_2_URL)))
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(MockMvcResultMatchers.content().string(
							StringUtils.asJsonString(
									prices
							)
					));
		
		
		
	}
}
