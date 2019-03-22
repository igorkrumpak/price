package si.iitech.price.acceptance;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import si.iitech.lib.security.impl.SecurityConstants;
import si.iitech.lib.util.StringUtils;
import si.iitech.price.entity.impl.EtPriceUser;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.exception.impl.RestException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, 
	properties = { "spring.h2.console.enabled=true",
					"spring.jpa.show-sql=true", "spring.jpa.properties.hibernate.format_sql=true",
					"logging.level.org.hibernate.SQL=debug", 
					"logging.level.org.hibernate.type.descriptor.sql=trace" })
@AutoConfigureMockMvc
public class FullAcceptanceTest {
	
	//register
	//login
	//add product mindfactory
	//add product mindfactory
	//run batch
	//retrive product from user

	private boolean registerAndLoginExecuted = false;
	private String token;
	
	@Autowired
	private MockMvc mvc;

	@Before
	public void registerAndLogin() throws Exception {
		if (registerAndLoginExecuted) return;
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/users/register")
					.content(StringUtils.asJsonString(new EtPriceUser("test@gmail.com", "test1234")))
					.contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
				    .andExpect(status().isOk());
		
		token = this.mvc.perform(
				MockMvcRequestBuilders
					.post("/login")
					.content(StringUtils.asJsonString(new EtPriceUser("test@gmail.com", "test1234")))
					.contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
				    .andExpect(status().isOk()).andReturn().getResponse().getHeader(SecurityConstants.TOKEN);
		
		registerAndLoginExecuted = true;
	}
	
	@Test
	public void acceptanceTest() throws Exception {
		this.mvc.perform(
				MockMvcRequestBuilders
				.post("/products")
				.header(SecurityConstants.TOKEN, token)
				.content(StringUtils.asJsonString(new EtProduct(null, "https://www.broken.de/product_info.php/6GB-KFA2-GeForce-GTX-1060-OC-Aktiv-PCIe-3-0-x16--Retail-_1286394.html")))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.content().string(StringUtils.asJsonString(new RestException("Source not found!"))));
		this.mvc.perform(
				MockMvcRequestBuilders
					.post("/products")
					.header(SecurityConstants.TOKEN, token)
					.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_3_URL)))
					.contentType(MediaType.APPLICATION_JSON)
				    .accept(MediaType.APPLICATION_JSON))
				    .andExpect(status().isOk());
		this.mvc.perform(
				MockMvcRequestBuilders
				.post("/products")
				.header(SecurityConstants.TOKEN, token)
				.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_2_URL)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		
		this.mvc.perform(
				MockMvcRequestBuilders
				.post("/products")
				.header(SecurityConstants.TOKEN, token)
				.content(StringUtils.asJsonString(new EtProduct(null, MindfactoryProduct.PRODUCT_2_URL)))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
		
		System.out.println("test");
	}
}
