package si.iitech.price.entity.impl;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import si.iitech.lib.entity.EtEntity;

@Entity(name = "SELECTED_PRODUCT")
public class EtSelectedProduct extends EtEntity {

	private EtPriceUser user;
	private EtProduct product;
	
	public EtSelectedProduct() {
		super();
	}

	public EtSelectedProduct(EtPriceUser user, EtProduct product) {
		this();
		this.user = user;
		this.product = product;
	}

	@ManyToOne
	public EtPriceUser getUser() {
		return user;
	}

	public void setUser(EtPriceUser user) {
		this.user = user;
	}

	@ManyToOne
	public EtProduct getProduct() {
		return product;
	}

	public void setProduct(EtProduct product) {
		this.product = product;
	}
}
