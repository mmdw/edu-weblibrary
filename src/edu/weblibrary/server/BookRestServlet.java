package edu.weblibrary.server;
import edu.weblibrary.server.persistence.cayenne.Book;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.apache.cayenne.CayenneDataObject;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;

public class BookRestServlet extends CommonRestServlet {
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
		
		query.addPrefetch("toAuthor");
		List<CayenneDataObject> dataList = context.performQuery(query);
		
		int total = totalRows().intValue();
		if (endRow > total - 1)	
			endRow = total - 1;		
		XMLFetchResponse xmlResponse = new XMLFetchResponse(0, startRow, endRow, total, sortBy);	
		
		for (CayenneDataObject dataObject: dataList) {
			Book book = (Book) dataObject;			
			
			String authorName = book.getToAuthor().getName();
			String authorSurame = book.getToAuthor().getSurname();	
			String authorBirthYear = book.getToAuthor().getBirthYear().toString();
			
			StringBuffer sb = new StringBuffer();
			sb.append(authorSurame).append(" ").append(authorName);
			//sb.append(" [").append(authorBirthYear).append("]");
			
			Map<String, String> attributes = readAttributes(dataObject);
			attributes.put("authorInitial", sb.toString());
			xmlResponse.addRecord(attributes);
		}
		
		xmlResponse.writeToStream(out);

		System.out.println("*** FETCH finished");
	}
}
