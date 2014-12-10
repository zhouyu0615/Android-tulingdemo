package com.example.tulingdemo;


public class ListData {
	
	private String content;
	
	private int flag;
	
	public static final int SEND=1;
	public static final int RECEIVER=2;
		
	public ListData(String data,int flag)
	{
		this.content=data;	
		this.flag=flag;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}

}
