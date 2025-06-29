package com.enth.ecomusic.model.dto;

import java.util.List;

public class ChartDTO {
	private List<String> labels;          // ["Week 1", "Week 2", ...]
    private List<ChartDatasetDTO> datasets;
	public ChartDTO(List<String> labels, List<ChartDatasetDTO> datasets) {
		super();
		this.labels = labels;
		this.datasets = datasets;
	}
	public List<String> getLabels() {
		return labels;
	}
	public List<ChartDatasetDTO> getDatasets() {
		return datasets;
	}
    
    
}
