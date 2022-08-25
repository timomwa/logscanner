package logscanner.main;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import logscanner.model.Report;
import logscanner.transfomers.Transformer;

public class Application {
	
	public static void main(String[] args) {
		//2010-10-06 09:14:26,019
		/*
		String date = "2010-10-06 09:14:26,019 [WorkerThread-2] INFO  [ServerSession]: Processing command object: {path=update##com.dn.gaverzicht.dms.filters.DocumentGroupFilter, type=evt-reg}";//WorkerThread-2]";// 09:14:26,019";
		Pattern DATE_PATTERN = Pattern.compile("(^\\d{4}-\\d{2}-\\d{2}"
				+ "\\s\\d{2}:\\d{2}:\\d{2},\\d{3}"
				+ ")\\s\\[(.+)\\]\\s(.+)\\s\\[(.+)\\](\\:\\s)(.+)$");
				//(\\:)\\s(.+)(\\:)\\s(.+)$");
		Matcher matcher = DATE_PATTERN.matcher(date);
		if(matcher.matches()) {
			System.out.println("1: "+ matcher.group(1));
			System.out.println("2: "+ matcher.group(2));
			System.out.println("3: ["+ matcher.group(3)+"]");
			System.out.println("4: ["+ matcher.group(4)+"]");
			System.out.println("5: ["+ matcher.group(5)+"]");
			System.out.println("6: ["+ matcher.group(6)+"]");
			System.out.println("7: ["+ matcher.group(7)+"]");
			System.out.println("8: ["+ matcher.group(8)+"]");
		}*/
		
		Report report = new Application().scan("/Users/mwangigikonyo/Documents/Food4Education/InterviewCaseStudy/server.log");
	}

	private Report scan(String theFile) {
		
		File file = null;
		LineIterator lineIterator = null;
		Report report  = null;
		
		try {
			file = new File(theFile);
			lineIterator = FileUtils.lineIterator(file, "UTF-8");
			
			report = process(lineIterator);
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				lineIterator.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return report;
	}

	@SuppressWarnings("deprecation")
	private Report process(LineIterator lineIterator) {
		try {
		    while (lineIterator.hasNext()) {
		        String line = lineIterator.nextLine();
		        Transformer.parse(line);
		    }
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
		    LineIterator.closeQuietly(lineIterator);
		}
		return new Report();
	}

}
