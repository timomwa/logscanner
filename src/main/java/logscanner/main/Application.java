package logscanner.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import logscanner.model.Rendering;
import logscanner.model.Report;
import logscanner.output.XMLWriterThread;
import logscanner.transfomers.FileChunkingThread;
import logscanner.transfomers.ListToFileThread;
import logscanner.transfomers.Transformer;

public class Application {
	
	private static final int CFG_MAX_QUEUE_SIZE = 1000;
	private static final int CFG_MAX_QUEUE_OFFER_TIME_MS = 0;
	private BlockingDeque<String> fileChunkingQueue = new LinkedBlockingDeque<String>(CFG_MAX_QUEUE_SIZE);
	
	
	private LinkedBlockingQueue<Rendering> xmlOutputStreamQueue = new LinkedBlockingQueue<Rendering>();
	private Transformer transformer = new Transformer(xmlOutputStreamQueue);
	private XMLWriterThread xmlWriterThread = new XMLWriterThread(xmlOutputStreamQueue);
	
	public void startThreads() {
		new Thread(xmlWriterThread).start();
	}
	
	public void stopThreads() {
		xmlWriterThread.setRun(false);
	}
	
	public static void main(String[] args) throws Exception {
		
		Application application = new Application();
		application.startThreads();
		application.scan("/Users/mwangigikonyo/Documents/Food4Education/InterviewCaseStudy/server.log");//args[0]);
		application.stopThreads();
		
		/*FileChunkingThread fCT = new FileChunkingThread("/Users/mwangigikonyo/Documents/Food4Education/InterviewCaseStudy/server.log");
		Thread fileChunkingThread = new Thread(fCT);
		fileChunkingThread.start();
		
		fileChunkingThread.join();
		
		System.out.println("Finished!");*/
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

	private Report process(LineIterator lineIterator) {
		try {
			int lineNumber = 0;
			int fileNumber = 0;
			
			List<String> batch = new ArrayList<String>();
			
		    while (lineIterator.hasNext()) {
		    	try {
			        String line = lineIterator.nextLine();
			        batch.add(line);
			        lineNumber++;
			        
		        	if(batch.size()==CFG_MAX_QUEUE_SIZE) {
		        		fileNumber++;
		        		ListToFileThread fcT = new ListToFileThread(batch, lineNumber, CFG_MAX_QUEUE_SIZE, fileNumber);
			        	new Thread(fcT).start();
			        	batch.clear();
			        	
		        		
			        }
			        
		    	}catch(Exception e) {
		    		e.printStackTrace();
		    	}
		    	
		    }
		    System.out.println("\n\n\t\t>>>Lines: "+lineNumber);
		}catch(java.lang.IllegalStateException ie) {
			if(ie.getMessage().contains("Queue full")) {
				System.out.println("Queu full");
			}
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
