package si.iitech.price.entities.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import si.iitech.entity.EtSource;
import si.iitech.price.parser.IPriceParser;

@Entity(name = "PRICE_SOURCE")

public class EtPriceSource extends EtSource<IPriceParser> {
	private List<EtProduct> products = new ArrayList<EtProduct>();

	@OneToMany(mappedBy = "priceSource", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<EtProduct> getProducts() {
		return products;
	}

	public void setProducts(List<EtProduct> products) {
		this.products = products;
	}

	public void addProduct(EtProduct product) {
		product.setPriceSource(this);
		getProducts().add(product);
	}
}
