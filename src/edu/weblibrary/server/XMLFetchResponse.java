package edu.weblibrary.server;

import java.io.Writer;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLFetchResponse {
	Document doc;
	
	Element rootElement;
	Element statusElement;
	Element startRowElement;
	Element endRowElement;		
	Element totalRowsElement;
	Element dataElement;
	
	Element sortByElement = null;
	
	public XMLFetchResponse(int status, int startRow, int endRow, int totalRows, String sortBy) {
		try {		
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();				
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			
			doc = builder.newDocument();
			
			rootElement      = doc.createElement("response");
			statusElement    = doc.createElement("status");
			startRowElement  = doc.createElement("startRow");
			endRowElement    = doc.createElement("endRow");
			totalRowsElement = doc.createElement("totalRows");
			dataElement      = doc.createElement("data");
			
			if (sortBy != null) {
				sortByElement = doc.createElement("sortBy");
				sortByElement.appendChild(doc.createTextNode(sortBy));
				rootElement.appendChild(sortByElement);
			}
			doc.appendChild(rootElement);
			rootElement.appendChild(statusElement);
			rootElement.appendChild(startRowElement);
			rootElement.appendChild(endRowElement);
			rootElement.appendChild(endRowElement);
			rootElement.appendChild(totalRowsElement);
			rootElement.appendChild(dataElement);
			
			statusElement.appendChild(doc.createTextNode(String.valueOf(status)));
			startRowElement.appendChild(doc.createTextNode(String.valueOf(startRow)));
			endRowElement.appendChild(doc.createTextNode(String.valueOf(endRow)));
			totalRowsElement.appendChild(doc.createTextNode(String.valueOf(totalRows)));
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public XMLFetchResponse(int status, int startRow, int endRow, int totalRows) {
		this(status, startRow, endRow, totalRows, (String)null);
	}
	
	public void addRecord(Map<String, String> attributes) {
		Element recordElement = doc.createElement("record");
		for (String key: attributes.keySet()) {
			Element attributeElement = doc.createElement(key);			
			attributeElement.appendChild(doc.createTextNode(attributes.get(key)));
			recordElement.appendChild(attributeElement);		
		}
		dataElement.appendChild(recordElement);	
	}
	
	public void writeToStream(Writer writer) {
		try {
			DOMSource domSource = new DOMSource(doc);		
			StreamResult streamResult = new StreamResult(writer);
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer serializer;
			serializer = tf.newTransformer();
			serializer.setOutputProperty("omit-xml-declaration", "yes");
			serializer.transform(domSource, streamResult);
		} catch (TransformerException e) {
			e.printStackTrace();
		} 	
	}
}