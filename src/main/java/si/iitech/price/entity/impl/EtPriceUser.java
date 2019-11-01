package si.iitech.price.entity.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import si.iitech.lib.entity.EtUser;

@Entity(name = "PRICE_USER")
public class EtPriceUser extends EtUser {

	private List<EtSelectedProduct> selectedProducts = new ArrayList<EtSelectedProduct>();

	public EtPriceUser() {
		super();
	}

	public EtPriceUser(String email, String password) {
		super(email, password);
	}

	@ManyToMany(mappedBy = "user", fetch = FetchType.LAZY)
	public List<EtSelectedProduct> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<EtSelectedProduct> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}
}
