package si.iitech.price.definition.impl;

import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import si.iitech.price.entity.impl.EtPriceSource;
import si.iitech.price.repository.PriceSourceRepository;

@Component
public class CompleteSourcesDefinition {

	private static final Logger LOG = Logger.getLogger("CompleteSourcesDefinition");

	@Autowired
	private PriceSourceRepository repository;

	@Autowired
	private EntityManager entityManager;

	@Transactional
	public void createDefinitions() {
		add(new MindfactoryDefinition().createDefinition());
	}

	private void add(EtPriceSource priceSource) {
		if (!repository.existsByUrl(priceSource.getUrl())) {
			entityManager.persist(priceSource);
		} else {
			LOG.info("Skipping existing source with url: " + priceSource.getUrl());
		}
	}
}
