package si.iitech.price.entity.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import si.iitech.lib.entity.EtEntity;

@Entity(name = "PRODUCT")
public class EtProduct extends EtEntity {

	private String title;
	private String url;
	private EtPriceSource priceSource;
	private List<EtPrice> prices = new ArrayList<EtPrice>();
	private List<EtUserProduct> userProducts = new ArrayList<EtUserProduct>();
	
	public EtProduct() {
		super();
	}

	public EtProduct(String title, String url) {
		super();
		this.title = title;
		this.url = url;
	}

	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@NotBlank(message = "Url is mandatory")
	@Column(nullable = false, unique = true)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	public EtPriceSource getPriceSource() {
		return priceSource;
	}

	public void setPriceSource(EtPriceSource priceSource) {
		this.priceSource = priceSource;
	}

	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
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
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "product")
	public List<EtUserProduct> getUserProducts() {
		return userProducts;
	}
	
	public void setUserProducts(List<EtUserProduct> userProducts) {
		this.userProducts = userProducts;
	}
}
