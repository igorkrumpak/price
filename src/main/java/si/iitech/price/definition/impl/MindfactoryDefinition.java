package si.iitech.price.definition.impl;

import si.iitech.price.parser.impl.MindfactoryParser;

public class MindfactoryDefinition extends PriceSourceDefinition {
	
	public static final String TITLE = "Mindfactory";

	@Override
	protected String getURL() {
		return "https://www.mindfactory.de";
	}
	
	@Override
	protected String getTitle() {
		return TITLE;
	}
	
	@Override
	protected String getParserClass() {
		return MindfactoryParser.class.getName();
	}
}
