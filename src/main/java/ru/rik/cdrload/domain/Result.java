package ru.rik.cdrload.domain;

public class Result {
	public String num;
	public String day;
	public int dstNum = 0;
	public int dstDistinct = 0;
	public int srcNum = 0;
	public int srcDistinct = 0;
	
	public Result() {	}

	public Result(String num) {
		super();
		this.num = num;
	}


	@Override
	public String toString() {
		return "Result [num=" + num + ", day=" + day + ", dstNum=" + dstNum + ", dstDistinct=" + dstDistinct
				+ ", srcNum=" + srcNum + ", srcDistinct=" + srcDistinct + "]";
	}

	public String toCsv() {
		return day + "," + num + "," + dstNum + "," + dstDistinct + "," + srcNum + "," + srcDistinct; 
	}
	

}
