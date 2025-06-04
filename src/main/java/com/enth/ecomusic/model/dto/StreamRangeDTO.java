package com.enth.ecomusic.model.dto;

public class StreamRangeDTO {
    private final long start;
    private final long end;
    private final long totalLength;

    public StreamRangeDTO(long start, long end, long totalLength) {
        this.start = start;
        this.end = end;
        this.totalLength = totalLength;
    }
    
    

    public long getStart() {
		return start;
	}



	public long getEnd() {
		return end;
	}



	public long getTotalLength() {
		return totalLength;
	}



	public long getContentLength() {
        return end - start + 1;
    }
}
