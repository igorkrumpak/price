package si.iitech.price.parser.impl;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import si.iitech.price.parser.IPriceParser;

public class MindfactoryParser implements IPriceParser {
	
	@Override
	public String getTitle(Document document) {
		Elements select = document.select("#cart_quantity > div > div.col-sm-6.col-md-4.fixheight-gallery > h1");
		return select.text();
	}

	//#priceCol > div.pprice > span.text-currency
	//#priceCol > div.pprice > span.specialPriceText > span
	//#priceCol > div.pprice > span.specialPriceText > span
	@Override
	public Double getPrice(Document document) {
		Elements select = document.select("#priceCol > div.pprice");
		Element element = select.get(0);
		element.getElementsByClass("pfin").remove();
		String price = element.text().replaceAll("[^0-9?!\\\\,]","");
		price = price.replaceAll(",", ".");
		return Double.valueOf(price);
	}

}
