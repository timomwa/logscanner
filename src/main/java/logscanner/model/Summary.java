package logscanner.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("summary")
public class Summary implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("count")
	private int count;
	
	@JsonProperty("duplicates")
	private int duplicates;
	
	@JsonProperty("unnecessary")
	private int unnecessary;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getDuplicates() {
		return duplicates;
	}
	public void setDuplicates(int duplicates) {
		this.duplicates = duplicates;
	}
	public int getUnnecessary() {
		return unnecessary;
	}
	public void setUnnecessary(int unnecessary) {
		this.unnecessary = unnecessary;
	}
	
}
