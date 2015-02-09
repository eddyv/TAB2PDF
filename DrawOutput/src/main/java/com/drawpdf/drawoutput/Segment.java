package com.drawpdf.drawoutput;

import java.util.ArrayList;

public class Segment {
	ArrayList<String> segment;

	public Segment() {
		segment = new ArrayList<String>();
	}

	public void add(String line) {
		segment.add(line);
		
	}

	public int size() {
		return segment.size();
	}

	public boolean isEmpty() {
		return segment.isEmpty();
	}

	public String get(int i) {
		return segment.get(i);
	}
}