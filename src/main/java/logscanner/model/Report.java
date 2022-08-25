package logscanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

@JsonRootName("report")
public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Rendering> renderings = new ArrayList<Rendering>();
	
	//@JsonProperty("summary")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Summary summary;

	public List<Rendering> getRenderings() {
		return renderings;
	}

	public void setRenderings(List<Rendering> renderings) {
		this.renderings = renderings;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	
	
	

}
