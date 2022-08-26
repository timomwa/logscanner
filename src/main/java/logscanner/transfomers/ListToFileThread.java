package logscanner.transfomers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import logscanner.utils.FileChunkingUtils;

public class ListToFileThread implements Runnable {

	private String fileName = null;

	private List<String> batch = null;

	public ListToFileThread(List<String> batch_, int lineNumber, int maxBatchSize, int fileNumber) {
		this.batch = new ArrayList<String>(batch_);
		this.fileName = FileChunkingUtils.createFileName(fileNumber, lineNumber, maxBatchSize);
	}

	public void run() {

		try {
			Path path = Paths.get(fileName);
			Files.write(path, batch);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
