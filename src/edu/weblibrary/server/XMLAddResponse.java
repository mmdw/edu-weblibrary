package edu.weblibrary.server;

import java.io.Writer;
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
import org.w3c.dom.Text;

public class XMLAddResponse {
	Document doc;
	
	Element rootElement;
	Element statusElement;
	Element dataElement;
	Text    statusValue;
	Element errorsElement;
	Element recordElement;
	
	public XMLAddResponse() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();				
			DocumentBuilder	builder = factory.newDocumentBuilder();			
			doc = builder.newDocument();
			
			rootElement = doc.createElement("response");
			statusElement = doc.createElement("status");
			statusValue = doc.createTextNode("");
			dataElement = null;
			recordElement = null;
			errorsElement = null;
			
			doc.appendChild(rootElement);
			rootElement.appendChild(statusElement);	
			statusElement.appendChild(statusValue);			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStatus(int status) {
		statusValue.replaceWholeText(String.valueOf(status));
	}
	
	public void addRecordAttribute(String name, String value) {
		if (dataElement == null) {
			dataElement = doc.createElement("data");
			recordElement = doc.createElement("record");
			dataElement.appendChild(recordElement);
			rootElement.appendChild(dataElement);
		}
		Element attributeElement = doc.createElement(name);			
		attributeElement.appendChild(doc.createTextNode(value));		
		recordElement.appendChild(attributeElement);		
	}
	
	public void addError(String attributeName, String message) {
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
