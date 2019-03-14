package si.iitech.price.definition.impl;

import si.iitech.price.entities.impl.EtPriceSource;

public abstract class PriceSourceDefinition {
	
	public EtPriceSource createDefinition() {
		EtPriceSource source = new EtPriceSource();
		source.setUrl(getURL());
		source.setTitle(getTitle());
		source.setParserClass(getParserClass());
		return source;
	}
	
	protected abstract String getTitle();

	protected abstract String getURL();
	
	protected abstract String getParserClass();

}
