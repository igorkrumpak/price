package si.iitech.price;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import si.iitech.lib.test.AbstractTest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, 
	properties = { "spring.h2.console.enabled=true",
					"spring.jpa.show-sql=true", "spring.jpa.properties.hibernate.format_sql=true",
					"logging.level.org.hibernate.SQL=debug", 
					"logging.level.org.hibernate.type.descriptor.sql=trace", 
					"spring.datasource.driver-class-name=org.h2.Driver",
					"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE",
					"spring.datasource.username=sa",
					"spring.datasource.password=sa",
					"spring.jpa.hibernate.ddl-auto=create-drop"
					})
@AutoConfigureMockMvc
@Ignore
public class PriceTest extends AbstractTest {

}
