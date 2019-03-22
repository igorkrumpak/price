package si.iitech.price.definition.impl;

import si.iitech.price.parser.impl.MindfactoryParser;

public class MindfactoryDefinition extends PriceSourceDefinition {
	
	public static final String TITLE = "Mindfactory";
	public static final String URL = "www.mindfactory.de";

	@Override
	protected String getURL() {
		return URL;
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
