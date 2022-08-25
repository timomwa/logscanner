package logscanner.transfomers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Transformer {
	public static final Pattern DATE_PATTERN = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2}"
			+ "\\s\\d{2}:\\d{2}:\\d{2},\\d{3}"
			+ ")\\s\\[(.+)\\]\\s(.+)\\s\\[(.+)\\](\\:\\s)(.+)$");
	public static final String START_RENDERING_STR = "startRendering";
	public static final String START_RENDERING_RETURN_STR = "Service startRendering returned";
	
	public static void parse(String line) throws Exception{
		Matcher matcher = DATE_PATTERN.matcher(line);
		if(matcher.matches()) {
		String logMessage = matcher.group(6);
			//System.out.println("1: "+ matcher.group(1));
			//System.out.println("2: "+ matcher.group(2));
			//System.out.println("3: ["+ matcher.group(3)+"]");
			//System.out.println("4: ["+ matcher.group(4)+"]");
			//System.out.println("5: ["+ matcher.group(5)+"]");
		if(logMessage.contains(START_RENDERING_STR) || logMessage.contains(START_RENDERING_RETURN_STR))
			System.out.println("6: ["+ logMessage+"]");
			//System.out.println("7: ["+ matcher.group(7)+"]");
			//System.out.println("8: ["+ matcher.group(8)+"]");
		}
		
	}
	public static void parsex(String line) throws Exception {
		if (line != null && !line.isEmpty() && !line.isBlank()) {
			try {
				
				int indexOfFirstOpeningBoxBrace = line.indexOf("[");
				int indexOfSecondOpeningBoxBrace = line.indexOf("]");

				String timeStamp = line.substring(0, indexOfFirstOpeningBoxBrace);
				String thread = line.substring(indexOfFirstOpeningBoxBrace+1, indexOfSecondOpeningBoxBrace);
				String debugLevel = line.substring(indexOfSecondOpeningBoxBrace+1);
				//System.out.println("Timestamp: " + timeStamp);
				//System.out.println("Thread: " + thread);
				System.out.println(debugLevel);
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("The line: "+line);
				//throw new Exception("");
			}
		}

	}

}
