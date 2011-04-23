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

public class XMLUpdateResponse {
	Document doc;
	Element rootElement;
	Element statusElement;
	Element errorsElement;
	Text    statusValue;
	
	public XMLUpdateResponse() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();				
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			
			doc = builder.newDocument();		
			rootElement  = doc.createElement("response");
			statusElement = doc.createElement("status");
			statusValue = doc.createTextNode("0");			
			errorsElement = null;
			
			doc.appendChild(rootElement);
			rootElement.appendChild(statusElement);
			statusElement.appendChild(statusValue);
			
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void setStatus(int status) {
		statusValue.replaceWholeText(String.valueOf(status));
	}
	
	public void addError(String attributeName, String errorMessage) {
		if (errorsElement == null) {
			errorsElement = doc.createElement("errors");
			rootElement.appendChild(errorsElement);
		}
		
		Element errorElement = doc.createElement(attributeName);
		Element errorMessageElement = doc.createElement("errorMessage");
		errorMessageElement.appendChild(doc.createTextNode(errorMessage));
		errorElement.appendChild(errorMessageElement);
		
		errorsElement.appendChild(errorElement);		
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
