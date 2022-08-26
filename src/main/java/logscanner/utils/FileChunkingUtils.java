package logscanner.utils;

public class FileChunkingUtils {
	
	public static String createFileName(int fileNumber, int lineNumber, int maxBatchSize) {
		int lineFrom = (lineNumber - maxBatchSize+1);
		return String.format("%s_%s-%s.tmp", fileNumber,lineFrom, lineNumber);
	}

}
