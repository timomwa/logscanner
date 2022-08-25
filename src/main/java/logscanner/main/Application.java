package logscanner.main;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import logscanner.model.Rendering;
import logscanner.model.Report;
import logscanner.output.XMLWriterThread;
import logscanner.transfomers.Transformer;

public class Application {
	
	private LinkedBlockingQueue<Rendering> xmlOutputStreamQueue = new LinkedBlockingQueue<Rendering>();
	private Transformer transformer = new Transformer(xmlOutputStreamQueue);
	private XMLWriterThread xmlWriterThread = new XMLWriterThread(xmlOutputStreamQueue);
	
	
	public void startThreads() {
		new Thread(xmlWriterThread).start();
	}
	
	public void stopThreads() {
		xmlWriterThread.setRun(false);
	}
	
	public static void main(String[] args) {
		if(args.length==0) {
			System.out.println("Usage: java -jar ./target/logscanner-0.0.1-SNAPSHOT.jar <LOG_FILE_PATH>"
					+ "\nWhere <LOG_FILE_PATH> is the location of the server log file");
			System.exit(-1);//Terminate
		}
		Application application = new Application();
		application.startThreads();
		application.scan(args[0]);
		application.stopThreads();
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
			int c = 0;
		    while (lineIterator.hasNext()) {
		        String line = lineIterator.nextLine();
		        transformer.parse(line, Transformer.LOG_LEVEL_FOCUS);
		        c++;
		    }
		    System.out.println("\n\n\t\t>>>Lines: "+c);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				lineIterator.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Report();
	}

}
