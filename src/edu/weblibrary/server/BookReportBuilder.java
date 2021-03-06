package edu.weblibrary.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lowagie.text.FontFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.SimpleFontFamily;
import net.sf.jasperreports.engine.query.JRJpaQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * <p>Генератор отчетов по книге.
 * <p>Отчеты могут быть в формате PDF файла и в виде HTML страницы. 
 * <p>Формат запроса: ?id=[bookId]&type=[pdf|html]
 * @author mmdw
 *
 */
public class BookReportBuilder extends HttpServlet {
	public static final Logger LOG = Logger.getLogger(BookReportBuilder.class);
	
	private final String REPORT_JASPER           = "report/book.jasper";
	private final String REPORT_JRXML            = "report/book.jrxml";
	private final String PERSISTENCE_UNIT = "pu4";
	private String PATH;
	
	enum ReportType {
		HTML_REPORT,
		PDF_REPORT
	}
	
	public void init(ServletConfig config) {
		PATH = config.getServletContext().getRealPath("/");
	}
	
	private void writeHtml(ServletOutputStream outStream, Map parameters, int id) throws JRException {		
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(PATH + REPORT_JASPER);
					
		JasperPrint jasperPrint = 
			JasperFillManager.fillReport(
				jasperReport, 
				parameters);
					
		JRHtmlExporter exporter = new JRHtmlExporter();
		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		
		exporter.exportReport();		
	}
	/**
	 * Записывает в поток отчет в виде файла PDF.
	 * @param out поток вывода
	 * @param parameters параметры заполнения отчета.
	 * @param id первичный ключ. Определяет запись в БД, которой заполняется отчет.
	 * @throws JRException
	 */
	
	private void writePdf(ServletOutputStream out, Map parameters, int id) throws JRException {
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(PATH + REPORT_JASPER);
		
		JasperPrint jasperPrint = 
			JasperFillManager.fillReport(
				jasperReport, 
				parameters);					
		
		JRPdfExporter exporter = new JRPdfExporter();		
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);	
		exporter.exportReport();		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		resp.setCharacterEncoding("utf8");
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT,new HashMap());
		EntityManager em = emf.createEntityManager();					
		try {
			String paramId = req.getParameter("id");
			String paramType = req.getParameter("type");
			
			if (paramId == null || paramType == null) {
				resp.getWriter().write("wrong params");
				return;
			}
			
			int id = Integer.parseInt(paramId);
			ReportType type = ReportType.valueOf(paramType.toUpperCase().concat("_REPORT"));
			
			Map parameters = new HashMap();
			parameters.put("BOOK_ID_PARAMETER", id);
			parameters.put(JRJpaQueryExecuterFactory.PARAMETER_JPA_ENTITY_MANAGER, em);	
			
			JasperFillManager.fillReportToFile(PATH + REPORT_JASPER, parameters);
			
			ServletOutputStream outStream = resp.getOutputStream();
			
			switch (type) {
			case HTML_REPORT:
				resp.setContentType("text/html");
				writeHtml(outStream, parameters, id);
				break;
			case PDF_REPORT:
				resp.setContentType("application/octet-stream"); 
				resp.setHeader("Content-Disposition", "attachment; filename=\"report" +	id + ".pdf\"");				
				writePdf(outStream, parameters, id);	
				break;

			default:
				break;
			}		
						
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
}