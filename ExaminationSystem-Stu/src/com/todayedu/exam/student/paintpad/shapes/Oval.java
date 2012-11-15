package com.todayedu.exam.student.paintpad.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.todayedu.exam.student.paintpad.interfaces.Shapable;

public class Oval extends ShapeAbstract {

	RectF mRectF;

	public Oval(Shapable paintTool) {
		super(paintTool);
		mRectF = new RectF();
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		super.draw(canvas, paint);
		mRectF.set(x1, y1, x2, y2);
		canvas.drawOval(mRectF, paint);
	}

	@Override
	public String toString() {
		return " oval";
	}
}
