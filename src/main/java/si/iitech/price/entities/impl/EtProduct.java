package si.iitech.price.entities.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import si.iitech.entity.EtEntity;

@Entity(name = "PRODUCT")
public class EtProduct extends EtEntity {

	private String title;
	private String url;
	private EtPriceSource priceSource;
	private List<EtPrice> prices = new ArrayList<EtPrice>();

	public EtProduct() {
		super();
	}

	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false, unique = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public EtPriceSource getPriceSource() {
		return priceSource;
	}

	public void setPriceSource(EtPriceSource priceSource) {
		this.priceSource = priceSource;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<EtPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<EtPrice> prices) {
		this.prices = prices;
	}

	public void addPrice(EtPrice price) {
		price.setProduct(this);
		getPrices().add(price);
	}

}
