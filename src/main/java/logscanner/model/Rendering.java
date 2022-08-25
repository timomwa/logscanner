package logscanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rendering implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String document;
	private int page;
	private String uid;
	private List<String> start = new ArrayList<String>();
	private List<String> get = new ArrayList<String>();
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
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
	public List<String> getStart() {
		return start;
	}
	public void setStart(List<String> start) {
		this.start = start;
	}
	public List<String> getGet() {
		return get;
	}
	public void setGet(List<String> get) {
		this.get = get;
	}
	
	
}
