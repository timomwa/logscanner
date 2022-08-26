package logscanner.transfomers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class FileChunkingThread implements Runnable {

	private static final int CFG_MAX_QUEUE_SIZE = 1000;
	private static final int CFG_MAX_QUEUE_OFFER_TIME_MS = 0;

	private String filename = null;
	private BlockingDeque<String> fileChunkingQueue = new LinkedBlockingDeque<String>(CFG_MAX_QUEUE_SIZE);

	public FileChunkingThread(String filename) {
		this.filename = filename;
	}

	public void run() {
		File file = null;
		LineIterator lineIterator = null;
		try {
			
			file = new File(this.filename);
			lineIterator = FileUtils.lineIterator(file, "UTF-8");
			
			int lineNumber = 0;
			int fileNumber = 0;
			
			while (lineIterator.hasNext()) {
				try {
					String line = lineIterator.nextLine();
					fileChunkingQueue.offerFirst(line, CFG_MAX_QUEUE_OFFER_TIME_MS, TimeUnit.MILLISECONDS);
					lineNumber++;
					
					if (fileChunkingQueue.size() == CFG_MAX_QUEUE_SIZE) {
						fileNumber++;
						createFileChunk(lineNumber, fileNumber);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				lineIterator.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void createFileChunk(int lineNumber, int fileNumber) {
		
		List<String> batch = new ArrayList<String>();
		fileChunkingQueue.drainTo(batch);
		
		ListToFileThread fcT = new ListToFileThread(batch, lineNumber, CFG_MAX_QUEUE_SIZE, fileNumber);
    	new Thread(fcT).start();

	}

}
