package si.iitech.price.parser;

import org.jsoup.nodes.Document;

import si.iitech.lib.parser.IParser;

public interface IPriceParser extends IParser {

	public Double getPrice(Document document);
}
