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

/**
 * Построитель XML ответов.
 * @author mmdw
 * @see com.smartgwt.client.data.RestDataSource
 */
public class XMLResponse {
	private Document doc;
	private Element rootElement;	
	private Element errorsElement    = null;	
	private Element statusElement    = null;
	private Element dataElement      = null;	
	
	private Element sortByElement    = null;
	private Element startRowElement  = null;
	private Element endRowElement    = null;		
	private Element totalRowsElement = null;
	
	public XMLResponse() {
		DocumentBuilder builder;
		DocumentBuilderFactory factory;
		try {		
			 factory = DocumentBuilderFactory.newInstance();		
			 builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException();			
		}
		
		doc = builder.newDocument();
		rootElement = doc.createElement("response");		
		
		doc.appendChild(rootElement);
	}	
	
	void setFetchInfo(int startRow, int endRow, int totalRows, String sortBy) {
		if (startRowElement == null) {
			startRowElement = doc.createElement("startRow");		
			rootElement.appendChild(startRowElement);
		}		
		startRowElement.setTextContent(String.valueOf(startRow));
		
		if (endRowElement == null) {
			endRowElement = doc.createElement("endRow");
			rootElement.appendChild(endRowElement);
		}
		endRowElement.setTextContent(String.valueOf(endRow));
		
		if (totalRowsElement == null) {
			totalRowsElement = doc.createElement("totalRows");
			rootElement.appendChild(totalRowsElement);
		}
		totalRowsElement.setTextContent(String.valueOf(totalRows));
		
		if (sortByElement != null && sortBy == null) {
			rootElement.removeChild(sortByElement);
		} 
		else
		if (sortBy != null){
			if (sortByElement == null) {
				sortByElement = doc.createElement("sortBy");
				rootElement.appendChild(sortByElement);
			}
			sortByElement.setTextContent(sortBy);
		}
	}
	
	/**
	 * Устанавливает статус ответа.
	 * @param status
	 * @see com.smartgwt.client.rpc.RPCResponse
	 */
	void setStatus(int status) {
		if (statusElement == null) {
			statusElement = doc.createElement("status");
			rootElement.appendChild(statusElement);
		} 
		statusElement.setTextContent(String.valueOf(status));		
	}
	
	/**
	 * Добавляет сообщение об ошибке в заданном атрибуте.
	 * @param attributeName 
	 * @param message
	 * @see com.smartgwt.client.data.RestDataSource
	 */
	void addErrorMessage(String attributeName, String message) {
		if (errorsElement == null) {
			errorsElement = doc.createElement("errors");
			rootElement.appendChild(errorsElement);
		}
		Element attributeElement = doc.createElement(attributeName);
		Element errorMessageElement = doc.createElement("errorMessage");
		
		attributeElement.appendChild(errorMessageElement);
		errorMessageElement.appendChild(doc.createTextNode(message));		
		errorsElement.appendChild(attributeElement);
	}	
	
	public void addRecord(Map<String, String> attributes) {
		if (dataElement == null) {
			dataElement = doc.createElement("data");
			rootElement.appendChild(dataElement);
		}
		Element recordElement = doc.createElement("record");
		for (String key: attributes.keySet()) {
			Element attributeElement = doc.createElement(key);			
			attributeElement.appendChild(doc.createTextNode(attributes.get(key)));
			recordElement.appendChild(attributeElement);		
		}
		dataElement.appendChild(recordElement);	
	}
	
	/**
	 * Записывает XML ответ в поток.
	 * @param writer
	 */
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
