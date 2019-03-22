package si.iitech.price.entity.impl;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import si.iitech.lib.entity.EtEntity;

@Entity(name = "USER_PRODUCT")
public class EtUserProduct extends EtEntity {

	private EtPriceUser user;
	private EtProduct product;
	
	public EtUserProduct() {
		super();
	}

	public EtUserProduct(EtPriceUser user, EtProduct product) {
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
