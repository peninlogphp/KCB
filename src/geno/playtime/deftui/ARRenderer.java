package geno.playtime.deftui;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*import com.beyondar.Constants;
import com.beyondar.engine.opengl.objects.Square;
import com.beyondar.objects.AbstractObject;
import com.beyondar.objects.World;
*/
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;

public class ARRenderer implements GLSurfaceView.Renderer, SensorEventListener {
	private float mAccelerometerValues[] = new float[3];
	private float mMagneticValues[] = new float[3];
	// private float mOrientationValues[] = new float[3];
	public float LOW_PASS_FILTER_CONSTANT = 0.9f;

	private float[] mProjection = new float[16];
	private float[] mModelView = new float[16];

	private float rotationMatrix[] = new float[16];
	private float remappedRotationMatrix[] = new float[16];

	private MatrixGrabber mg = new MatrixGrabber();

	float[] linear_acceleration = new float[3];

	public Vec3 postionCamera;

	int mWidth, mHeight;

	//uncomment private World mWorld;

	// TESTING

	private boolean drawLine = false;

	//uncomment private Square squareTest = new Square();
	Vec3 worldPosTest;

	/**
	 * Keeps track of the Projection matrix calculated on the last draw frame
	 */
	private float[] lastProjectionMat = null;

	/**
	 * Keeps track of the model view matrix calculated on the last frame
	 */
	private float[] lastModelViewMat = null;

	public ARRenderer() {
		postionCamera = new Vec3(0, 0, 0);

		this.lastProjectionMat = new float[16];
		this.lastModelViewMat = new float[16];

	}

	/**
	 * Record the current modelView matrix state. Has the side effect of setting
	 * the current matrix state to GL_MODELVIEW
	 * 
	 * @param gl
	 *            context
	 */
	public float[] getCurrentModelView(GL10 gl) {
		float[] modelView = new float[16];
		getMatrix(gl, GL10.GL_MODELVIEW, modelView);
		return modelView;
	}

	/**
	 * Record the current projection matrix state. Has the side effect of
	 * setting the current matrix state to GL_PROJECTION
	 * 
	 * @param gl
	 *            context
	 */
	public float[] getCurrentProjection(GL10 gl) {
		float[] projection = new float[16];
		getMatrix(gl, GL10.GL_PROJECTION, projection);
		return projection;
	}

	/**
	 * Fetches a specific matrix from opengl
	 * 
	 * @param gl
	 *            context
	 * @param mode
	 *            of the matrix
	 * @param mat
	 *            initialized float[16] array to fill with the matrix
	 */
	private void getMatrix(GL10 gl, int mode, float[] mat) {
		MatrixTrackingGL gl2 = (MatrixTrackingGL) gl;
		gl2.glMatrixMode(mode);
		gl2.getMatrix(mat, 0);
	}

	/**
	 * Define the world where the objects are stored.
	 * 
	 * @param world
	 */
	/*public void setWorld(World world) {
		mWorld = world;
	}*/

	public void onDrawFrame(GL10 gl) {
		// Get rotation matrix from the sensor
		SensorManager.getRotationMatrix(rotationMatrix, null,
				mAccelerometerValues, mMagneticValues);
		// // As the documentation says, we are using the device as a compass in
		// // landscape mode
		SensorManager.remapCoordinateSystem(rotationMatrix,
				SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
				remappedRotationMatrix);

		// Clear color buffer
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		// Load remapped matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glLoadMatrixf(remappedRotationMatrix, 0);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		// gl.glEnable(GL10.GL_BLEND); //Turn Blending On
		// gl.glDisable(GL10.GL_DEPTH_TEST); //Turn Depth Testing Off

		// getCurrentProjection(gl);
		// getCurrentModelView(gl);

		mProjection = getCurrentProjection(gl);
		mModelView = getCurrentModelView(gl);

		// Store projection and modelview matrices
		mg.getCurrentState(gl);

		if (drawLine) {
			// gl.glBegin(GL10.GL_LINES);
			// gl.glVertex3f(100.0f, 100.0f, 0.0f); // origin of the line
			// glVertex3f(200.0f, 140.0f, 5.0f); // ending point of the line

			// gl.glTranslatef((float) worldPosTest.X(), (float)
			// worldPosTest.Y(),
			// (float) worldPosTest.Z());

			// gl.glTranslatef(5.2f,5f,0);
			// squareTest.draw(gl);
			//
			// gl.glTranslatef((float) -worldPosTest.X(), (float)-
			// worldPosTest.Y(),
			// (float) -worldPosTest.Z());

			// worldPosTest.SetX(worldPosTest.X()+0.1f);
			// worldPosTest.SetY(worldPosTest.Y()+0.1f);
			// worldPosTest.SetZ(worldPosTest.Z()+0.1f);

		}
		/*if (mWorld != null) {
			mWorld.draw(gl);
		}*/

	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		/*
		 * Set our projection matrix. This doesn't have to be done each time we
		 * draw, but usually a new projection needs to be set when the viewport
		 * is resized.
		 */
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		// gl.glFrustumf(-ratio, ratio, -1, 1, 1f, 100);

		GLU.gluPerspective(gl, 45.0f, ratio, 0.1f, 100.0f);
		mWidth = width;
		mHeight = height;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		/*
		 * By default, OpenGL enables features that improve quality but reduce
		 * performance. One might want to tweak that especially on software
		 * renderer.
		 */
		gl.glDisable(GL10.GL_DITHER);

		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading
		gl.glClearDepthf(1.0f); // Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		gl.glEnable(GL10.GL_BLEND); // Enable blending
		// GL10.GL_REPLACE;
		// gl.glDisable(GL10.GL_DEPTH_TEST); // Disable depth test
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Set The Blending
														// Function For
														// Translucency
		gl.glAlphaFunc(GL10.GL_NOTEQUAL, 1);

		/*
		 * Some one-time OpenGL initialization can be made here probably based
		 * on features of this particular context
		 */
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		// TODO What is the best choice?
		// Really Nice Perspective Calculations
		// gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		// gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
		// GL10.GL_REPLACE);

		gl.glClearColor(0, 0, 0, 0);

		/*Log.d(Constants.TAG, "Loading textures...");
		mWorld.loadTextures(gl);
		Log.d(Constants.TAG, "DONE");*/

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {

			switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:

				// Filter the values
				// mAccelerometerValues = event.values.clone();
				mAccelerometerValues = LowPassfilter(event.values.clone(),
						mAccelerometerValues);
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:

				// Filter the values
				// mMagneticValues = event.values.clone();
				mMagneticValues = LowPassfilter(event.values.clone(),
						mMagneticValues);
				break;
			// case Sensor.TYPE_ORIENTATION:
			// mOrientationValues = event.values.clone();
			// Log.d("BeyondAR",
			// "mOrientationValues: "
			// + mOrientationValues[0] + " "
			// + mOrientationValues[1] + " "
			// + mOrientationValues[2]);
			// break;
			default:
				break;
			}
		}
	}

	/**
	 * Low pass filter for the sensor data
	 * 
	 * @param valuesToFilter
	 *            The values to filter
	 * @param oldValues
	 *            The previous values
	 * @return The filtered values
	 */
	public float[] LowPassfilter(float[] valuesToFilter, float[] oldValues) {

		if (oldValues == null) {
			oldValues = valuesToFilter;
			return valuesToFilter;
		}

		float[] output = new float[3];
		output[0] = LOW_PASS_FILTER_CONSTANT * valuesToFilter[0]
				+ (1 - LOW_PASS_FILTER_CONSTANT) * oldValues[0];
		output[1] = LOW_PASS_FILTER_CONSTANT * oldValues[1]
				+ (1 - LOW_PASS_FILTER_CONSTANT) * valuesToFilter[1];
		output[2] = LOW_PASS_FILTER_CONSTANT * oldValues[2]
				+ (1 - LOW_PASS_FILTER_CONSTANT) * valuesToFilter[2];

		return output;
	}

	/*public AbstractObject getObjectRay(Vec3 vec) {

		return null;
	}*/

	public float[] getViewRay(float[] tap) {
		// view port
		int[] viewport = { 0, 0, mWidth, mHeight };

		// far eye point
		float[] eye = new float[4];
		GLU.gluUnProject(tap[0], mHeight - tap[1], 0.9f, mg.mModelView, 0,
				mg.mProjection, 0, viewport, 0, eye, 0);

		// fix
		if (eye[3] != 0) {
			eye[0] = eye[0] / eye[3];
			eye[1] = eye[1] / eye[3];
			eye[2] = eye[2] / eye[3];
		}

		// ray vector
		float[] ray = { (float) (eye[0] - postionCamera.X()),
				(float) (eye[1] - postionCamera.Y()),
				(float) (eye[2] - postionCamera.Z()), 0.0f };
		return ray;
	}

	/**
	 * Calculates the transform from screen coordinate system to world
	 * coordinate system coordinates for a specific point, given a camera
	 * position.
	 * 
	 * @param touch
	 *            Vec2 point of screen touch, the actual position on physical
	 *            screen (ej: 160, 240)
	 * @param cam
	 *            camera object with x,y,z of the camera and screenWidth and
	 *            screenHeight of the device.
	 * @return position in WCS.
	 */
	public Vec3 GetWorldCoords(Vec2 touch) {
		// Initialize auxiliary variables.
		Vec3 worldPos = new Vec3();

		// SCREEN height & width (ej: 320 x 480)
		// float screenW = cam.GetScreenWidth();
		// float screenH = cam.GetScreenHeight();

		// Auxiliary matrix and vectors
		// to deal with ogl.
		float[] invertedMatrix, transformMatrix, normalizedInPoint, outPoint;
		invertedMatrix = new float[16];
		transformMatrix = new float[16];
		normalizedInPoint = new float[4];
		outPoint = new float[4];

		// Invert y coordinate, as android uses
		// top-left, and ogl bottom-left.
		int oglTouchY = (int) (mHeight - touch.Y());

		/*
		 * Transform the screen point to clip space in ogl (-1,1)
		 */
		normalizedInPoint[0] = (float) ((touch.X()) * 2.0f / mWidth - 1.0);
		normalizedInPoint[1] = (float) ((oglTouchY) * 2.0f / mHeight - 1.0);
		normalizedInPoint[2] = -1.0f;
		normalizedInPoint[3] = 1.0f;

		/*
		 * Obtain the transform matrix and then the inverse.
		 */

		Matrix.multiplyMM(transformMatrix, 0, mProjection, 0, mModelView, 0);

		Matrix.invertM(invertedMatrix, 0, transformMatrix, 0);

		/*
		 * Apply the inverse to the point in clip space
		 */
		Matrix.multiplyMV(outPoint, 0, invertedMatrix, 0, normalizedInPoint, 0);

		if (outPoint[3] == 0.0) {
			// Avoid /0 error.
			Log.e("World coords", "ERROR!");
			return worldPos;
		}

		// Divide by the 3rd component to find
		// out the real position.
		worldPos.Set(outPoint[0] / outPoint[3], outPoint[1] / outPoint[3],
				outPoint[2] / outPoint[3]);

		worldPosTest = worldPos;
		return worldPos;
	}

	/*public World getWorld() {
		return mWorld;
	}*/

	// http://ovcharov.me/2011/01/14/android-opengl-es-ray-picking/
	// http://magicscrollsofcode.blogspot.com/2010/10/3d-picking-in-android.html
}
