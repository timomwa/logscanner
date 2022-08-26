package logscanner.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import logscanner.model.Rendering;
import logscanner.model.Report;
import logscanner.model.Summary;
import logscanner.transfomers.Transformer;

public class Main {
	
	
	private Map<String, List<Rendering>> replicateRenderings = new HashMap<String, List<Rendering>>();
	private XmlMapper xmlMapper = new XmlMapper();
	private File xmlReportFile = new File("report.xml");

	public static void main(String[] args) {
		if(args==null || args.length<1) {
			System.out.println("Usage: java -jar ./target/logscanner-0.0.1-SNAPSHOT.jar <LOG_FILE_PATH>\n"
					+ "Where <LOG_FILE_PATH> is the location of the log file");
			System.exit(-1);
		}
			
		Main main = new Main();
		main.process(args[0]);

	}

	
	/**
	 * Iterate through file
	 * @param sourceFile
	 */
	private void process(String sourceFile) {
		
		File file = null;
		LineIterator lineIterator = null;
		try {

			file = new File(sourceFile);
			lineIterator = FileUtils.lineIterator(file, "UTF-8");
			
			
			String logMessage = "";
			String threadID = "";
			String timeStamp = "";
			int documentID = -1;
			int pageIndex = -1;
			String uuid = "";
			Rendering rendering = null;
			
			int x = 0;

			while (lineIterator.hasNext()) {
				
				try {
					
					String line = lineIterator.nextLine();
					Matcher lineMatcher = Transformer.LOG_ENTRY_PATTERN.matcher(line);
					if (lineMatcher.matches()) {
						
						logMessage = lineMatcher.group(6);
						
						if (logMessage.contains(Transformer.EXECUTING_REQ_START_RENDERING)) {
							
							
							
							threadID = lineMatcher.group(2);
							timeStamp = lineMatcher.group(1);
							
							Matcher docIdAndPageMatcher = Transformer.DOC_ID_AND_PAGE_PATTERN.matcher(logMessage);
							if (docIdAndPageMatcher.matches()) {
								documentID = Integer.valueOf(docIdAndPageMatcher.group(2));
								pageIndex = Integer.valueOf(docIdAndPageMatcher.group(4));
								rendering = new Rendering(documentID, pageIndex);
								rendering.setProcessingThreadName(threadID);
								rendering.getStart().add(Transformer.SIMPLE_DATE_FORMAT.parse(timeStamp));
							}
							
							
						}
						
						
						if (logMessage.contains(Transformer.RENDERING_RETURN_SIGNAL)) {
							Matcher UUID_SEARCH_MATCHER = Transformer.UUID_SEARCH_PATTERN.matcher(line);
							if(UUID_SEARCH_MATCHER.matches()) {
								uuid = UUID_SEARCH_MATCHER.group(2);
								rendering.setUid(uuid);
								
								String endTimeStamp = lineMatcher.group(1);
								rendering.getGet().add(Transformer.SIMPLE_DATE_FORMAT.parse(endTimeStamp));
								
								addToMap(rendering);
								x++;
							}
						}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
			
			createReport();

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				lineIterator.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void createReport() throws JsonGenerationException, JsonMappingException, IOException {
		
		Report report = new Report();
		Summary summary = new Summary();
		
		int totalRenderings = 0;
		int duplicates = 0;
		int unnecessary = 0;
		for(Map.Entry<String, List<Rendering>> entry : replicateRenderings.entrySet()) {
			try {
				String docId_uid = entry.getKey();
				String documentId = docId_uid.split("_")[0];
				String uid = docId_uid.split("_")[1];
				
				List<Rendering> rendering = entry.getValue();
				
				if(rendering.size()>1)
					duplicates++;
				
				Rendering ren = new Rendering();
				ren.setDocument(Integer.valueOf(documentId));
				ren.setUid(uid);
				
				for(Rendering render : rendering) {
					
					
					ren.getStart().addAll( render.getStart() );
					ren.getGet().addAll( render.getGet() );
					
					if(ren.getGet().size()<1)
						unnecessary++;
					
					report.getRenderings().add( ren );
					
					totalRenderings++;
					
				}
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		summary.setCount( totalRenderings );
		summary.setDuplicates(duplicates );
		summary.setUnnecessary(unnecessary);
		report.setSummary(summary);
		
		
		//Pretty format
		xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
		xmlMapper.writeValue(xmlReportFile, report);
		
		
	}
	

	/**
	 * Add to maps
	 * @param rendering
	 */
	private void addToMap(Rendering rendering) {
		
		//Form key
		String key = String.format("%s_%s", rendering.getDocument(),rendering.getUid() );
		List<Rendering> replicates = replicateRenderings.get( key );
		if(replicates == null) {
			replicates = new ArrayList<Rendering>();
			replicates.add( rendering );
			replicateRenderings.put(key, replicates);
		}else {
			replicates.add( rendering );
			replicateRenderings.put(key, replicates);
		}
	}

}
