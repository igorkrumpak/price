package si.iitech.price.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import si.iitech.price.definition.impl.CompleteSourcesDefinition;

@Component
public class AppStartupRunner implements ApplicationRunner {

	@Autowired
	private CompleteSourcesDefinition completeSourcesDefinition;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		completeSourcesDefinition.createDefinitions();
	}
}
