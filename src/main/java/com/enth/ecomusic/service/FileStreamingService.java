package com.enth.ecomusic.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import com.enth.ecomusic.model.dto.StreamRangeDTO;

public class FileStreamingService {

	public FileStreamingService() {

	}

	public StreamRangeDTO parseRangeHeader(String rangeHeader, long fileLength) {
		if (rangeHeader == null || !rangeHeader.startsWith("bytes=")) {
			return new StreamRangeDTO(0, fileLength - 1, fileLength); // full content
		}

		try {
			String[] parts = rangeHeader.substring(6).split("-");
			long start = Long.parseLong(parts[0].trim());
			long end = (parts.length > 1 && !parts[1].isEmpty()) ? Long.parseLong(parts[1].trim()) : fileLength - 1;
			if (end >= fileLength)
				end = fileLength - 1;
			if (start > end)
				start = 0; // fallback in case of bad range
			return new StreamRangeDTO(start, end, fileLength);
		} catch (Exception e) {
			// fallback: serve full file if parsing fails
			return new StreamRangeDTO(0, fileLength - 1, fileLength);
		}
	}

	public void streamFile(File file, StreamRangeDTO range, OutputStream out) throws IOException {
		try (RandomAccessFile input = new RandomAccessFile(file, "r")) {
			input.seek(range.getStart());
			byte[] buffer = new byte[4096];
			long remaining = range.getContentLength();

			while (remaining > 0) {
				int read = input.read(buffer, 0, (int) Math.min(buffer.length, remaining));
				if (read == -1)
					break;
				out.write(buffer, 0, read);
				remaining -= read;
			}
		}
	}
}
