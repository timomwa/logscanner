package logscanner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Report implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<Rendering> renderings = new ArrayList<Rendering>();

	public List<Rendering> getRenderings() {
		return renderings;
	}

	public void setRenderings(List<Rendering> renderings) {
		this.renderings = renderings;
	}

}
