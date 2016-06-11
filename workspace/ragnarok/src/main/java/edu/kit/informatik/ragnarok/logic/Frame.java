package edu.kit.informatik.ragnarok.logic;

import java.util.Set;
import java.util.HashSet;

public class Frame {

	private Vec2D vec1;
	private Vec2D vec2;

	public Frame(Vec2D v1, Vec2D v2) {
		this.SetFrame(v1, v2);
	}

	public Frame(float x1, float y1, float x2, float y2) {
		this.SetFrame(new Vec2D(x1, y1), new Vec2D(x2, y2));
	}

	public void SetFrame(Vec2D v1, Vec2D v2) {
		this.vec1 = v1;
		this.vec2 = v2;
	}

	public boolean collidesWith(Frame otherFrame) {
		/*
		leftBottom1.X < rightTop2.X &&
        leftBottom1.Y < rightTop2.Y &&
        leftBottom2.X < rightTop1.X &&
        leftBottom2.Y < rightTop1.Y;
		*/
		
		return
				vec1.getX() < otherFrame.vec2.getX() &&
				vec1.getY() < otherFrame.vec2.getY() &&
				vec2.getX() > otherFrame.vec1.getX() &&
				vec2.getX() > otherFrame.vec1.getX();
	}

	public boolean collidesWith(Vec2D vec) {
		return vec.getX() > vec1.getX() && vec.getX() < vec2.getX()
				&& vec.getY() > vec1.getY() && vec.getY() < vec2.getY();
	}

}
