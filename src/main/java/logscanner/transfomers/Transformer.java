package logscanner.transfomers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logscanner.model.Rendering;

public class Transformer {
	
	private LinkedBlockingQueue<Rendering> xmlOutQueue;
	
	public Transformer(LinkedBlockingQueue<Rendering> xmlOutQueue) {
		this.xmlOutQueue = xmlOutQueue;
	}
	
	public static final String LOG_LEVEL_FOCUS = "INFO";
	public static volatile Map<Integer, Rendering> documentIdVsCountMap = new ConcurrentHashMap<Integer, Rendering>();
	public static final Pattern LOG_ENTRY_PATTERN = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2}"
			+ "\\s\\d{2}:\\d{2}:\\d{2},\\d{3}" + ")\\s\\[(.+)\\]\\s(.+)\\s\\[(.+)\\](\\:\\s)(.+)$");
	public static final Pattern DOC_ID_AND_PAGE_PATTERN = Pattern.compile("(.+)\\[(\\d+)(.+)(\\d+)\\](.+)$");
	public static final String START_RENDERING_STR = "startRendering";
	public static final String START_RENDERING_RETURN_STR = "Service startRendering returned";
	public static final String EXECUTING_REQ_START_RENDERING = "Executing request startRendering";// with arguments";
	public static final String RENDERING_RETURN_SIGNAL = "Service startRendering returned";
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.ENGLISH);
	public static final Pattern UUID_SEARCH_PATTERN = Pattern.compile("(.+)\\s(\\d+\\-\\d+)$");

	public void parse(String line, String logLevelFocus) throws Exception {

		if (line.contains(logLevelFocus)) {
			
			String threadID = "";
			String timeStamp = "";
			int documentID = -1;
			int pageIndex = -1;
			String uuid = "";
			Rendering rendering = null;

			Matcher lineMatcher = LOG_ENTRY_PATTERN.matcher(line);
			if (lineMatcher.matches()) {
				String logMessage = lineMatcher.group(6);
				threadID = lineMatcher.group(2);
				timeStamp = lineMatcher.group(1);
				//System.out.println("Start " + start);
				// System.out.println("2: "+ lineMatcher.group(2));
				// System.out.println("3: ["+ lineMatcher.group(3)+"]");
				// System.out.println("4: ["+ lineMatcher.group(4)+"]");
				// System.out.println("5: ["+ lineMatcher.group(5)+"]");
				// Document start processing
				if (logMessage.contains(EXECUTING_REQ_START_RENDERING)) {
					// System.out.println("logMessage: "+ logMessage+"]");
					Matcher docIdAndPageMatcher = DOC_ID_AND_PAGE_PATTERN.matcher(logMessage);
					if (docIdAndPageMatcher.matches()) {
						documentID = Integer.valueOf(docIdAndPageMatcher.group(2));
						pageIndex = Integer.valueOf(docIdAndPageMatcher.group(4));
						rendering = new Rendering(documentID, pageIndex);
						rendering.setProcessingThreadName(threadID);
						rendering.getStart().add(SIMPLE_DATE_FORMAT.parse(timeStamp));
						documentIdVsCountMap.put(documentID, rendering);
						
						this.xmlOutQueue.add(rendering);
						//System.out.println(rendering);

						// System.out.println("documentID: "+ documentID+"]");
						// System.out.println("pageIndex: "+ pageIndex+"]");
					}
				}

				if (logMessage.contains(RENDERING_RETURN_SIGNAL)) {
					
					Matcher UUID_SEARCH_MATCHER = UUID_SEARCH_PATTERN.matcher(line);
					if(UUID_SEARCH_MATCHER.matches()) {
						uuid = UUID_SEARCH_MATCHER.group(2);
						rendering.setUid(uuid);
						//System.out.println(uuid);
					}

				}

				// ||
				// logMessage.contains(START_RENDERING_STR) ||
				// logMessage.contains(START_RENDERING_RETURN_STR))

				// System.out.println("7: ["+ matcher.group(7)+"]");
				// System.out.println("8: ["+ matcher.group(8)+"]");
			}

		}

	}

	public static void parsex(String line) throws Exception {
		if (line != null && !line.isEmpty() && !line.isBlank()) {
			try {

				int indexOfFirstOpeningBoxBrace = line.indexOf("[");
				int indexOfSecondOpeningBoxBrace = line.indexOf("]");

				String timeStamp = line.substring(0, indexOfFirstOpeningBoxBrace);
				String thread = line.substring(indexOfFirstOpeningBoxBrace + 1, indexOfSecondOpeningBoxBrace);
				String debugLevel = line.substring(indexOfSecondOpeningBoxBrace + 1);
				// System.out.println("Timestamp: " + timeStamp);
				// System.out.println("Thread: " + thread);
				System.out.println(debugLevel);
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("The line: " + line);
				// throw new Exception("");
			}
		}

	}

}
