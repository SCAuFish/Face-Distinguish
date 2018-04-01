package com.amazonaws.samples;

public class Student {
	String portrait;
	int X, Y;
	boolean attend;
	boolean deviation;
	public Student(String path, int X, int Y){
		this.portrait = path;
		this.X = X;
		this.Y = Y;
		this.attend = false;
		this.deviation = false; //
	}
}
