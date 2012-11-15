package com.todayedu.exam.student.paintpad.painttools;

import android.graphics.Paint;

import com.todayedu.exam.student.paintpad.interfaces.ToolInterface;

//∆’Õ®ª≠± 
public class PlainPen extends PenAbstract implements ToolInterface {
	public PlainPen(int size, int penColor) {
		this(size, penColor, Paint.Style.STROKE);
	}

	public PlainPen(int size, int penColor, Paint.Style style) {
		super(size, penColor, style);
	}

	@Override
	public String toString() {
		return "\tplainPen: " + "\tshap: " + currentShape + "\thasDraw: "
				+ hasDraw() + "\tsize: " + penSize + "\tstyle:" + style;
	}
}
