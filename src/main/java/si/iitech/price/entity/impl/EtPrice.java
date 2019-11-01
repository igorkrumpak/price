package si.iitech.price.entity.impl;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import si.iitech.lib.entity.EtEntity;

@Entity(name = "PRICE")
public class EtPrice extends EtEntity {

	private EtProduct product;
	private Double price;
	private Date priceDate;

	public EtPrice() {
		super();
	}

	@Column(precision = 2)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(nullable = false)
	public Date getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	public EtProduct getProduct() {
		return product;
	}

	public void setProduct(EtProduct product) {
		this.product = product;
	}
}
