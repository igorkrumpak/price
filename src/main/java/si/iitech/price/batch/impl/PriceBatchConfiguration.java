package si.iitech.price.batch.impl;

import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

import si.iitech.price.entity.impl.EtPrice;
import si.iitech.price.entity.impl.EtProduct;
import si.iitech.price.repository.PriceRepository;
import si.iitech.price.repository.ProductRepository;

@Configuration
@EnableBatchProcessing
public class PriceBatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PriceRepository priceRepository;

	@Bean
	public PriceItemProcesor processor() {
		return new PriceItemProcesor();
	}

	@Bean
	public RepositoryItemReader<EtProduct> reader() {
		RepositoryItemReader<EtProduct> reader = new RepositoryItemReader<>();
		reader.setRepository(productRepository);
		reader.setMethodName("findAll");
		HashMap<String, Direction> sorts = new HashMap<>();
		sorts.put("oid", Direction.DESC);
		reader.setSort(sorts);
		return reader;
	}

	@Bean
	public RepositoryItemWriter<EtPrice> writer() {
		RepositoryItemWriter<EtPrice> writer = new RepositoryItemWriter<>();
		writer.setRepository(priceRepository);
		writer.setMethodName("save");
		return writer;
	}

	@Bean
	public Step step1(RepositoryItemWriter<EtPrice> writer) {
		return stepBuilderFactory
				.get("step1")
				.<EtProduct, EtPrice>chunk(10)
				.reader(reader())
				.processor(processor())
				.faultTolerant()
				.writer(writer)
				.build();
	}

	@Bean(name = "priceBatch")
	public Job importUserJob(Step step1) {
		return jobBuilderFactory
				.get("priceBatch")
				.incrementer(new RunIdIncrementer())
				.flow(step1)
				.end()
				.build();
	}
}
