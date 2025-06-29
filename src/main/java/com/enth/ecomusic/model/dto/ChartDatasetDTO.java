package com.enth.ecomusic.model.dto;

import java.util.List;

public class ChartDatasetDTO {
    private String label;                 // e.g. "Play Count"
    private List<Integer> data;          // [120, 130, 90, ...]
    
	public ChartDatasetDTO(String label, List<Integer> data) {
		super();
		this.label = label;
		this.data = data;
	}
	public String getLabel() {
		return label;
	}
	public List<Integer> getData() {
		return data;
	}
    
    
}