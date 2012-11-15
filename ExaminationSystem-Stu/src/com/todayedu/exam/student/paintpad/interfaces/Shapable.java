package com.todayedu.exam.student.paintpad.interfaces;

import com.todayedu.exam.student.paintpad.painttools.FirstCurrentPosition;

import android.graphics.Path;

public interface Shapable {
	public Path getPath();

	public FirstCurrentPosition getFirstLastPoint();

	public void setShap(ShapesInterface shape);
}
