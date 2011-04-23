package edu.weblibrary.server;

import java.io.IOException;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.DataObjectUtils;
import org.apache.cayenne.DeleteDenyException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.map.SQLResult;
import org.apache.cayenne.query.SQLTemplate;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;
import org.apache.cayenne.validation.BeanValidationFailure;
import org.apache.cayenne.validation.ValidationException;
import org.apache.cayenne.validation.ValidationFailure;

public class CommonRestServlet extends HttpServlet {
	private enum Operation {
		FETCH, ADD, REMOVE, UPDATE
	}
	
	private SQLTemplate countQuery;
	protected ObjectContext context;
	private TreeSet<String> attributeNames;
	protected Class<? super CayenneDataObject> DataObjectClass;
	
	private String ID_PK_COLUMN;
	private static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	protected Number totalRows()	{
		return (Number) DataObjectUtils.objectForQuery(context, countQuery);
	}
	
	private Map<String, String> getAttributesFromRequest(HttpServletRequest req, TreeSet<String> set) {
		Map<String, String> result = new TreeMap<String, String>();
		
		for (String key: set)		
			if (req.getParameterMap().containsKey(key))			
				result.put(key, req.getParameter(key));
		
		return result;		
	}
	
	protected void writeAttributes(CayenneDataObject cdo, Map<String, String> attributes) {
		for (String name: attributes.keySet())	{
			String val = attributes.get(name);
			if (val != null && val.equals("null")) val = null;
			cdo.writeProperty(name, val);
		}		
	}
	
	protected Map<String, String> readAttributes(CayenneDataObject cdo) {
		Map<String, String> attributeMap = new TreeMap<String, String>();
		for (String name: attributeNames)	{					
			Object val = cdo.readProperty(name);
			
			if (val instanceof Date)
				val = ISO8601FORMAT.format(val);
			
			if (val != null)
				attributeMap.put(name, val.toString());				
		}	
		
		return attributeMap;		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		resp.setCharacterEncoding("utf8");
		resp.setContentType("text/xml");
		PrintWriter out = resp.getWriter();
		
		for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();) {
			String param = e.nextElement();
			
			System.out.print(param + " =");
			for(String s: req.getParameterValues(param))
				System.out.print(" ".concat(s));
			System.out.println();
		}			
				
		Operation op = Operation.valueOf(req.getParameter("_operationType").toUpperCase());
		
		switch (op) {
			case FETCH: 
				fetch(out, 
					  Integer.parseInt(req.getParameter("_startRow")),
					  Integer.parseInt(req.getParameter("_endRow")),
					  req.getParameter("_sortBy"));
				break;
			case UPDATE:
				update(out, getAttributesFromRequest(req, attributeNames));
				break;
			case REMOVE: 
				remove(out, getAttributesFromRequest(req, attributeNames)); 
				break;
			case ADD: 
				add(out, getAttributesFromRequest(req, attributeNames));									
		}
   }

	protected void remove(PrintWriter out, Map<String, String> map) {		
		SelectQuery query = 
			new SelectQuery(DataObjectClass, ExpressionFactory.matchExp(ID_PK_COLUMN, map.get(ID_PK_COLUMN)));		
		
		CayenneDataObject a = (CayenneDataObject) context.performQuery(query).get(0);
		
		try {
			context.deleteObject(a);
			context.commitChanges();
		} catch (DeleteDenyException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			
			out.print("<response><status>-4</status></response>");
			context.rollbackChanges();
			return;
		}		
		
		out.print("<response><status>0</status></response>");
	}

	protected void update(PrintWriter out, Map<String, String> attributes) {		
		SelectQuery updateQuery = new SelectQuery(DataObjectClass, 
				ExpressionFactory.matchExp(ID_PK_COLUMN, attributes.get(ID_PK_COLUMN)));
		
		CayenneDataObject dataObject = (CayenneDataObject) context.performQuery(updateQuery).get(0);
		XMLUpdateResponse xmlResponse = new XMLUpdateResponse();
		
		writeAttributes(dataObject, attributes);
		
		try	{		
			context.commitChanges();
		} catch (ValidationException e) {				
			xmlResponse.setStatus(-4);	
			
			for(ValidationFailure vf: e.getValidationResult().getFailures())			
				xmlResponse.addError(((BeanValidationFailure)vf).getProperty(), (String) vf.getError());
			
			context.rollbackChanges();
			xmlResponse.writeToStream(out);
			return;
		}
		
		xmlResponse.setStatus(0);
		xmlResponse.writeToStream(out);
	}

	//?  <record field1="value" field2="value" />
	protected void fetch(PrintWriter out, int startRow, int endRow, String sortBy) {		
		System.out.println("*** FETCH started");
		SelectQuery query = new SelectQuery(DataObjectClass);
		query.setFetchLimit(endRow - startRow + 1);
		query.setFetchOffset(startRow);
		
		if (sortBy != null)	{
			if (sortBy.charAt(0) == '-')
				query.addOrdering(sortBy.substring(1), SortOrder.ASCENDING);
			else			
				query.addOrdering(sortBy, SortOrder.DESCENDING);							
		}
		
		List<CayenneDataObject> dataList = context.performQuery(query);
		
		int total = totalRows().intValue();
		if (endRow > total - 1)	
			endRow = total - 1;		
		XMLFetchResponse xmlResponse = new XMLFetchResponse(0, startRow, endRow, total, sortBy);	
		
		for (CayenneDataObject dataObject: dataList)			
			xmlResponse.addRecord(readAttributes(dataObject));
		
		xmlResponse.writeToStream(out);

		System.out.println("*** FETCH finished");
	}
	
	protected void add(PrintWriter out, Map<String, String> attributes)
	{
		CayenneDataObject rel = (CayenneDataObject) context.newObject(DataObjectClass);	
		XMLAddResponse xmlResponse = new XMLAddResponse();
		
		xmlResponse.setStatus(0);
				
		writeAttributes(rel, attributes);	
		
		try	{		
			context.commitChanges();			
		} catch (ValidationException e) {	
			xmlResponse.setStatus(-4);
			
			
			for(ValidationFailure vf: e.getValidationResult().getFailures())			
				xmlResponse.addError(((BeanValidationFailure)vf).getProperty(), (String) vf.getError());
			
			context.rollbackChanges();
			xmlResponse.writeToStream(out);
			return;				
		} catch (Exception ex) {	
			ex.printStackTrace();
			
			xmlResponse.setStatus(-4);
			context.rollbackChanges();
			xmlResponse.writeToStream(out);
			return;		
		}
		
		for (String i: attributeNames) {
			Object value = rel.readProperty(i);
			if (value == null) value = new String("null");			
			xmlResponse.addRecordAttribute(i, value.toString());
		}	
		
		xmlResponse.writeToStream(out);
	}
	
	public void init(ServletConfig config) throws ServletException	{		
		try {
			DataObjectClass =
				(Class<? super CayenneDataObject>) Class.forName((String) config.getInitParameter("DataObjectClass"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		context = DataContext.createDataContext();		
		
		ObjEntity entity = context.getEntityResolver().lookupObjEntity(DataObjectClass);
		attributeNames = new TreeSet<String>(entity.getAttributeMap().keySet());		
		
		countQuery = new SQLTemplate(DataObjectClass, "select count(*) from " + entity.getDbEntityName());		
		SQLResult resultDescriptor = new SQLResult();
		resultDescriptor.addColumnResult("S");
		countQuery.setResult(resultDescriptor);
		
		try {			
			ID_PK_COLUMN = (String) DataObjectClass.getField("ID_PK_COLUMN").get(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
