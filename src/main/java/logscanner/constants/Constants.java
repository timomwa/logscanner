package logscanner.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import logscanner.model.Rendering;

public class Constants {
	
	
	
	public static final String LOG_LEVEL_FOCUS = "INFO";
	public static volatile Map<Integer, Rendering> documentIdVsCountMap = new ConcurrentHashMap<Integer, Rendering>();
	public static final Pattern LOG_ENTRY_PATTERN = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2}"
			+ "\\s\\d{2}:\\d{2}:\\d{2},\\d{3}" + ")\\s\\[(.+)\\]\\s(.+)\\s\\[(.+)\\](\\:\\s)(.+)$");
	public static final Pattern DOC_ID_AND_PAGE_PATTERN = Pattern.compile("(.+)\\[(\\d+)(.+)(\\d+)\\](.+)$");
	public static final String START_RENDERING_STR = "startRendering";
	public static final String START_RENDERING_RETURN_STR = "Service startRendering returned";
	public static final String EXECUTING_REQ_START_RENDERING = "Executing request startRendering";
	public static final String RENDERING_RETURN_SIGNAL = "Service startRendering returned";
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.ENGLISH);
	public static final Pattern UUID_SEARCH_PATTERN = Pattern.compile("(.+)\\s(\\d+\\-\\d+)$");


}
