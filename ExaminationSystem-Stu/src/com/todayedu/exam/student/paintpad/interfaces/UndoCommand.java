package com.todayedu.exam.student.paintpad.interfaces;

public interface UndoCommand {
	public void undo();

	public void redo();

	public boolean canUndo();

	public boolean canRedo();
}
