package ru.rik.cdrload.domain;

import java.util.ArrayList;
import java.util.List;

public class Numbers {
	private final List<String> dsts;
	private final List<String> srcs;
	
	public Numbers() {	
		dsts = new ArrayList<>();
		srcs = new ArrayList<>();
	}
	
	public synchronized void addSrc(String n) {
		srcs.add(n);
	}

	public synchronized void addDst(String n) {
		dsts.add(n);
	}

	public synchronized List<String> getDsts() {
		return dsts;
	}

	public synchronized List<String> getSrcs() {
		return srcs;
	}
	

}
