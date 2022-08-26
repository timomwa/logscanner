package logscanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JsonRootName("rendering")
public class Rendering implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Rendering() {}
	
	public Rendering(int document, int page) {
		this.document = document;
		this.page = page;
	}
	
	@JsonIgnore
	private String processingThreadName;
	
	@JsonProperty("document")
	private int document;
	
	@JsonProperty("page")
	private int page;
	
	@JsonProperty("uid")
	private String uid;
	
	@JsonProperty("start")
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private List<Date> start = new ArrayList<Date>();
	
	@JsonProperty("get")
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
	private List<Date> get = new ArrayList<Date>();
	
	public int getDocument() {
		return document;
	}
	public void setDocument(int document) {
		this.document = document;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public List<Date> getStart() {
		return start;
	}
	public void setStart(List<Date> start) {
		this.start = start;
	}
	public List<Date> getGet() {
		return get;
	}
	public void setGet(List<Date> get) {
		this.get = get;
	}
	
	public String getProcessingThreadName() {
		return processingThreadName;
	}

	public void setProcessingThreadName(String processingThreadName) {
		this.processingThreadName = processingThreadName;
	}

	@Override
	public String toString() {
		return "Rendering [processingThreadName=" + processingThreadName + ", document=" + document + ", page=" + page
				+ ", uid=" + uid + ", start=" + start + ", get=" + get + "]";
	}
	
}
