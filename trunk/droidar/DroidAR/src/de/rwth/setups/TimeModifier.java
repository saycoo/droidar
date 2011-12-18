package de.rwth.setups;

import gl.Renderable;

import javax.microedition.khronos.opengles.GL10;

import system.ParentStack;
import util.Calculus;
import util.Vec;
import worldData.RenderableEntity;
import worldData.Updateable;
import worldData.Visitor;
import android.util.Log;

public class TimeModifier implements RenderableEntity {

	private static final String LOG_TAG = "TimeModifier";
	private static final float TRESHOLD = 0.001f;
	private static final float DEFAULT_ADJUSTMENT_SPEED = 4;
	private RenderableEntity myChild;
	private float myCurrentFactor;
	private float myNewFactor;
	private float myAdjustmentSpeed;

	public TimeModifier(float timeFactor) {
		this(timeFactor, DEFAULT_ADJUSTMENT_SPEED);
	}

	public TimeModifier(float timeFactor, float adjustmentSpeed) {
		myCurrentFactor = timeFactor;
		myNewFactor = timeFactor;
		myAdjustmentSpeed = adjustmentSpeed;
	}

	@Override
	public boolean update(float timeDelta, Updateable parent,
			ParentStack<Updateable> stack) {
		if (different(myCurrentFactor, myNewFactor))
			myCurrentFactor = Calculus.morphToNewValue(timeDelta
					* myAdjustmentSpeed, myNewFactor, myCurrentFactor);
		else
			myCurrentFactor = myNewFactor;
		if (myCurrentFactor == 0)
			return true;
		if (myChild != null)
			return myChild.update(timeDelta * myCurrentFactor, parent, stack);

		Log.e(LOG_TAG, "Child was not set");
		return false;
	}

	private boolean different(float a, float b) {
		return Math.abs(a - b) > TRESHOLD;
	}

	@Override
	public boolean accept(Visitor visitor) {
		if (myChild != null)
			return myChild.accept(visitor);

		Log.e(LOG_TAG, "Child was not set");
		return false;
	}

	@Override
	public void render(GL10 gl, Renderable parent, ParentStack<Renderable> stack) {
		if (myChild != null)
			myChild.render(gl, parent, stack);
		else
			Log.e(LOG_TAG, "Child was not set");
	}

	public void setChild(RenderableEntity l) {
		myChild = l;
	}

	public void setTimeFactor(float newTimeFactor) {
		myNewFactor = newTimeFactor;
	}

	public float getTimeFactor() {
		return myNewFactor;
	}

}