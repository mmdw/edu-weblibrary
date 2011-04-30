package edu.weblibrary.server;

import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.junit.Test;

public class XMLResponseTest {
	@Test
	public void testEmpty() {
		XMLResponse resp = new XMLResponse();
		StringWriter sw = new StringWriter();
		resp.writeToStream(sw);
		assertTrue(sw.toString(), sw.toString().equals("<response/>"));
	}
	
	@Test
	public void testError() {
		XMLResponse resp = new XMLResponse();
		resp.addErrorMessage("name", "message");
		StringWriter sw = new StringWriter();
		resp.writeToStream(sw);
		assertTrue(sw.toString(), sw.toString().equals(
				"<response><errors><name><errorMessage>message"+
				"</errorMessage></name></errors></response>"));	
	}
	
	@Test
	public void testStatus() {
		XMLResponse resp = new XMLResponse();
		resp.setStatus(-4);
		StringWriter sw = new StringWriter();
		resp.writeToStream(sw);
		assertTrue(sw.toString(), sw.toString().equals(
				"<response><status>-4</status></response>"));			
	}
	
	@Test
	public void testStatusAndError() {
		XMLResponse resp = new XMLResponse();
		resp.setStatus(-4);
		resp.addErrorMessage("name", "message");		
		StringWriter sw = new StringWriter();
		resp.writeToStream(sw);
		assertTrue(sw.toString(), sw.toString().equals(
				"<response><status>-4</status><errors><name><errorMessage>message"+
				"</errorMessage></name></errors></response>"));			
	}
	
}
