package geno.playtime.deftui;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.Matrix;


public final class SceneState {
	
	static final float angleFactor = 0.2f;
	public float dx, dy;
	public float dxSpeed, dySpeed;
	//GlMatrix baseMatrix = new GlMatrix();
float currentMatrix[] = new float[16];
boolean dampenDy = false;
public SceneState(){
	Matrix.setIdentityM(currentMatrix, 0);
}
	boolean lighting = true;
	int filter = 2;
	int objectIdx = 1;
	
	public void toggleLighting() {
		lighting = !lighting;
	}

	public void switchToNextFilter() {
		filter = (filter + 1) % 3;
	}

	public void switchToNextObject() {
		objectIdx = (objectIdx + 1) % 6;
	}
	
	public void saveRotation() {
		if (dx != 0) {
			//GlMatrix rotation = new GlMatrix();
			//rotation.rotate(dx * angleFactor, 0, 1, 0);
			//baseMatrix.premultiply(rotation);
			//Matrix.rotateM(currentMatrix, 0, dx * angleFactor, 0, 1, 0);
		}
		dx = 0.0f;
	}

	
	void rotateModel(GL10 gl) {
		if (dx != 0) {
			//gl.glRotatef(dx * angleFactor, 0, 1, 0);
			gl.glRotatef(dx, 0, 1, 0);

		}
		if (dy != 0) {
			//gl.glRotatef(dx * angleFactor, 0, 1, 0);
			gl.glRotatef(dy * angleFactor, 0, 1, 0);

		}
		//gl.glMultMatrixf(currentMatrix, 0);
	}

	public void dampenSpeed(long deltaMillis) {
		if (dxSpeed != 0.0f) {
			dxSpeed *= (1.0f - 0.001f * deltaMillis);
			if (Math.abs(dxSpeed) < 0.001f) dxSpeed = 0.0f;
		}
	}
	public void dampenSpeed() {
		if (dx != 0.0f) {
			dx *= (0.9f);
			if (Math.abs(dx) < 0.001f) dx = 0.0f;
		}
		if (dampenDy) {
			if (dy != 0.0f) {
				dy *= (0.9f);
				if (Math.abs(dy) < 0.001f) dy = 0.0f;
			}
		}
	}
	public void setDampenDy(boolean dampenDy) {
		this.dampenDy = dampenDy;
	}
}
