package logscanner.output;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import logscanner.model.Rendering;

public class XMLWriterThread implements Runnable{
	
	private LinkedBlockingQueue<Rendering> renderingQueue = null;
	private File xmlReportFile = new File("report.xml");
	private XmlMapper xmlMapper = new XmlMapper();
	
	private boolean run = true;
	
	public XMLWriterThread(LinkedBlockingQueue<Rendering> renderingQueue) {
		this.renderingQueue = renderingQueue;
	}
	
	public void run() {
		System.out.println("Start!");
		
		while(run || !renderingQueue.isEmpty()) {
			try {
				Rendering rendering = renderingQueue.poll(1000, TimeUnit.MILLISECONDS);
				if(rendering!=null) {
					System.out.println(rendering);
					writeXML(rendering);
				}	
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("End!");
		
	}

	private void writeXML(Rendering rendering) throws JsonGenerationException, JsonMappingException, IOException {
		xmlMapper.writeValue(xmlReportFile, rendering);
		
	}

	public void setRun(boolean run) {
		this.run = run;
	}
	
	

}
