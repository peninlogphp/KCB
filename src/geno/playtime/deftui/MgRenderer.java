package geno.playtime.deftui;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kcb.android.StanChartHome;




import kcb.android.R;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class MgRenderer implements Renderer {
	
	public static final  int SEND_MONEY = 0;
	public static final  int RECEIVE_MONEY = 1;
	public static final  int LOCATOR = 2;
	public static final byte TRANSACTION_HISTORY = 3;
	public static final byte BENEFICIARIES = 4;
	public static final byte INQUIRIES_N_FAQS = 5;
	public static final byte MY_ACCOUNT = 6;

	public static final double Y_AXIS_THETA = 2*Math.PI/7;
	public static final double Z_AXIS_THETA = Math.PI/4;
	
	public static final float RADIUS = 0.22f;
	public static final float centerX = 0.0f;
	public static final float centerY = 0.6f;

	private static float[] musicCoords = new float[] {
		 -1.205f,  -0.75f,  0.47f,
		 -1.205f, 0.75f,  0.47f,
		 -0.675f, 0.75f, 1,
		-0.675f, -0.75f, 1
	};

	// The order we like to connect them.
	private short[] indices = { 0, 3, 1, 3, 2, 1 };
	private short[] indicesBack = { 1, 3, 0, 1, 2, 3 };

	// Our index buffer.
	private ShortBuffer indexBuffer;
	private ShortBuffer indexBackBuffer;
	
	private static float[] bankCoords = new float[] {
		 -0.65f,  -0.75f,  1,
		 -0.65f, 0.75f,  1,
		 0.65f, 0.75f, 1,
		 0.65f, -0.75f, 1
	};
	
	private static float[] infoCoords = new float[] {
		 0.675f,  -0.75f,  1,
		 0.675f, 0.75f,  1,
		 1.205f, 0.75f, 0.47f,
		 1.205f, -0.75f, 0.47f
	};
	
	private static float[] musicBackCoords = new float[] {
		 0.675f,  -0.75f,  -1,
		 0.675f, 0.75f,  -1,
		 1.205f, 0.75f, -0.47f,
		 1.205f, -0.75f, -0.47f
	};
	private static float[] bankBackCoords = new float[] {
		 -0.65f,  -0.75f,  -1,
		 -0.65f, 0.75f,  -1,
		 0.65f, 0.75f, -1,
		 0.65f, -0.75f, -1
	};
	private static float[] infoBackCoords = new float[] {
		 -1.205f,  -0.75f,  -0.47f,
		 -1.205f, 0.75f,  -0.47f,
		 -0.675f, 0.75f, -1,
		-0.675f, -0.75f, -1
	};
	
	static float currentCenter[] = new float[] {0.0f, 0.6f};
	private static float[] menuItem1Coords;	
	private static float[] menuItem2Coords;
	private static float[] menuItem3Coords;
	private static float[] menuItem4Coords;
	private static float[] menuItem5Coords;
	private static float[] menuItem6Coords;
private static float[] menuItem7Coords;

	private static float[] rightMenuItem1Coords;
	private static float[] rightMenuItem2Coords;
	private static float[] rightMenuItem3Coords;
	private static float[] rightMenuItem4Coords;
	private static float[] rightMenuItem5Coords;
	private static float[] rightMenuItem6Coords;
	private static float[] rightMenuItem7Coords;
	private static float[] leftMenuItem1Coords;
	private static float[] leftMenuItem2Coords;
	private static float[] leftMenuItem3Coords;
	private static float[] leftMenuItem4Coords;
	private static float[] leftMenuItem5Coords;
	private static float[] leftMenuItem6Coords;
	private static float[] leftMenuItem7Coords;

	float textureCoordinates[] = {
			0, 1,
			0, 0,
			1, 0,
			1, 1 
            };
	
	float textureBackCoordinates[] = {
			1, 1,
			1, 0,
			0, 0,
			0, 1 
            };
	
	private FloatBuffer musicVertexBfr;
	private FloatBuffer musicBackVertexBfr;
	private FloatBuffer bankVertexBfr;
	private FloatBuffer bankBackVertexBfr;
	private FloatBuffer infoVertexBfr;
	private FloatBuffer infoBackVertexBfr;
	private FloatBuffer textureBfr;	
	private FloatBuffer textureBackBfr;	
	private FloatBuffer menuItem1VertexBfr;
	private FloatBuffer menuItem2VertexBfr;
	private FloatBuffer menuItem3VertexBfr;
	private FloatBuffer menuItem4VertexBfr;
	private FloatBuffer menuItem5VertexBfr;
	private FloatBuffer menuItem6VertexBfr;
	private FloatBuffer menuItem7VertexBfr;

	private FloatBuffer rightMenuItem1VertexBfr;
	private FloatBuffer rightMenuItem2VertexBfr;
	private FloatBuffer rightMenuItem3VertexBfr;
	private FloatBuffer rightMenuItem4VertexBfr;
	private FloatBuffer rightMenuItem5VertexBfr;
	private FloatBuffer rightMenuItem6VertexBfr;
	private FloatBuffer rightMenuItem7VertexBfr;
	private FloatBuffer leftMenuItem1VertexBfr;
	private FloatBuffer leftMenuItem2VertexBfr;
	private FloatBuffer leftMenuItem3VertexBfr;
	private FloatBuffer leftMenuItem4VertexBfr;
	private FloatBuffer leftMenuItem5VertexBfr;
	private FloatBuffer leftMenuItem6VertexBfr;
	private FloatBuffer leftMenuItem7VertexBfr;

	public final SceneState sceneState = new SceneState();
	private MatrixGrabber matrixGrabber = new MatrixGrabber();
int screenWidth, screenHeight;
Bitmap sendMoney, receiveMoney, locator,
transactionHistory, beneficiaries, inquiriesNfaqs, myAccount;
int[] textures = new int[7];
private Context context;
private float t, currentT = 1.1f;
private byte selection;
private int iconPlacement[] = {0, 1, 2};
private int xShift, yShift;
public float dz = -3.0f;
public boolean scrolled = false;
	public MgRenderer(int screenWidth, int screenHeight, Context context) {
		//this.screenWidth = screenWidth;
		//this.screenHeight = screenHeight;
		this.context = context;
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		initMenuItems();
		ByteBuffer vbb = ByteBuffer.allocateDirect(musicCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		musicVertexBfr = vbb.asFloatBuffer();
		musicVertexBfr.put(musicCoords);
		musicVertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(musicBackCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		musicBackVertexBfr = vbb.asFloatBuffer();
		musicBackVertexBfr.put(musicBackCoords);
		musicBackVertexBfr.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ibb = ByteBuffer.allocateDirect(indicesBack.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBackBuffer = ibb.asShortBuffer();
		indexBackBuffer.put(indicesBack);
		indexBackBuffer.position(0);
		
		vbb = ByteBuffer.allocateDirect(bankCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		bankVertexBfr = vbb.asFloatBuffer();
		bankVertexBfr.put(bankCoords);
		bankVertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(bankBackCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		bankBackVertexBfr = vbb.asFloatBuffer();
		bankBackVertexBfr.put(bankBackCoords);
		bankBackVertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(infoCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		infoVertexBfr = vbb.asFloatBuffer();
		infoVertexBfr.put(infoCoords);
		infoVertexBfr.position(0);

		vbb = ByteBuffer.allocateDirect(infoBackCoords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		infoBackVertexBfr = vbb.asFloatBuffer();
		infoBackVertexBfr.put(infoBackCoords);
		infoBackVertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(menuItem1Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem1VertexBfr = vbb.asFloatBuffer();
		menuItem1VertexBfr.put(menuItem1Coords);
		menuItem1VertexBfr.position(0);		
		
		vbb = ByteBuffer.allocateDirect(menuItem2Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem2VertexBfr = vbb.asFloatBuffer();
		menuItem2VertexBfr.put(menuItem2Coords);
		menuItem2VertexBfr.position(0);	
		
		vbb = ByteBuffer.allocateDirect(menuItem3Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem3VertexBfr = vbb.asFloatBuffer();
		menuItem3VertexBfr.put(menuItem3Coords);
		menuItem3VertexBfr.position(0);	
		
		vbb = ByteBuffer.allocateDirect(menuItem4Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem4VertexBfr = vbb.asFloatBuffer();
		menuItem4VertexBfr.put(menuItem4Coords);
		menuItem4VertexBfr.position(0);	
		
		vbb = ByteBuffer.allocateDirect(menuItem5Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem5VertexBfr = vbb.asFloatBuffer();
		menuItem5VertexBfr.put(menuItem5Coords);
		menuItem5VertexBfr.position(0);	
		
		vbb = ByteBuffer.allocateDirect(menuItem6Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem6VertexBfr = vbb.asFloatBuffer();
		menuItem6VertexBfr.put(menuItem6Coords);
		menuItem6VertexBfr.position(0);	
		
		vbb = ByteBuffer.allocateDirect(menuItem7Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		menuItem7VertexBfr = vbb.asFloatBuffer();
		menuItem7VertexBfr.put(menuItem7Coords);
		menuItem7VertexBfr.position(0);	
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem1Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem1VertexBfr = vbb.asFloatBuffer();
		rightMenuItem1VertexBfr.put(rightMenuItem1Coords);
		rightMenuItem1VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem2Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem2VertexBfr = vbb.asFloatBuffer();
		rightMenuItem2VertexBfr.put(rightMenuItem2Coords);
		rightMenuItem2VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem3Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem3VertexBfr = vbb.asFloatBuffer();
		rightMenuItem3VertexBfr.put(rightMenuItem3Coords);
		rightMenuItem3VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem4Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem4VertexBfr = vbb.asFloatBuffer();
		rightMenuItem4VertexBfr.put(rightMenuItem4Coords);
		rightMenuItem4VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem5Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem5VertexBfr = vbb.asFloatBuffer();
		rightMenuItem5VertexBfr.put(rightMenuItem5Coords);
		rightMenuItem5VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem6Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem6VertexBfr = vbb.asFloatBuffer();
		rightMenuItem6VertexBfr.put(rightMenuItem6Coords);
		rightMenuItem6VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(rightMenuItem7Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		rightMenuItem7VertexBfr = vbb.asFloatBuffer();
		rightMenuItem7VertexBfr.put(rightMenuItem7Coords);
		rightMenuItem7VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem1Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem1VertexBfr = vbb.asFloatBuffer();
		leftMenuItem1VertexBfr.put(leftMenuItem1Coords);
		leftMenuItem1VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem2Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem2VertexBfr = vbb.asFloatBuffer();
		leftMenuItem2VertexBfr.put(leftMenuItem2Coords);
		leftMenuItem2VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem3Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem3VertexBfr = vbb.asFloatBuffer();
		leftMenuItem3VertexBfr.put(leftMenuItem3Coords);
		leftMenuItem3VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem4Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem4VertexBfr = vbb.asFloatBuffer();
		leftMenuItem4VertexBfr.put(leftMenuItem4Coords);
		leftMenuItem4VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem5Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem5VertexBfr = vbb.asFloatBuffer();
		leftMenuItem5VertexBfr.put(leftMenuItem5Coords);
		leftMenuItem5VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem6Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem6VertexBfr = vbb.asFloatBuffer();
		leftMenuItem6VertexBfr.put(leftMenuItem6Coords);
		leftMenuItem6VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(leftMenuItem7Coords.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		leftMenuItem7VertexBfr = vbb.asFloatBuffer();
		leftMenuItem7VertexBfr.put(leftMenuItem7Coords);
		leftMenuItem7VertexBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		textureBfr = vbb.asFloatBuffer();
		textureBfr.put(textureCoordinates);
		textureBfr.position(0);
		
		vbb = ByteBuffer.allocateDirect(textureBackCoordinates.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		textureBackBfr = vbb.asFloatBuffer();
		textureBackBfr.put(textureBackCoordinates);
		textureBackBfr.position(0);

	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0, 0, 0, 0);

		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		//gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		loadBitmaps();
		loadTextures(gl);

	}
	
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	
		// draw music
		//gl.glScalef(6, 6, 1);
		if(Math.abs(sceneState.dx) < 26.565f &&
				Math.abs(sceneState.dy * 0.2f) < 26.565f && !scrolled){
			dz = dz+0.01f;
		    if(dz > -3.0f){
		    	dz = -3.0f;
		    }		
		}
		
		gl.glTranslatef(0, 0, dz);
		sceneState.rotateModel(gl);
		matrixGrabber.getCurrentState(gl);

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, musicVertexBfr);

		// Enable the color array buffer to be used during rendering.
		//gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		// Point out the where the color buffer is.
		//gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
		
		// Telling OpenGL to enable textures.
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// Tell OpenGL to enable the use of UV coordinates.
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Telling OpenGL where our UV coordinates are.
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[iconPlacement[0]]);
		//gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
			//	GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		if(iconPlacement[0] == 1)
		{
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem1VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[SEND_MONEY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem2VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[RECEIVE_MONEY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem3VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[LOCATOR]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem4VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[TRANSACTION_HISTORY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem5VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BENEFICIARIES]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem6VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[INQUIRIES_N_FAQS]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem7VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[MY_ACCOUNT]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		}else if(iconPlacement[0] == 2)
		{
		/*	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem1VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CHEQUE_B00K_REQUEST]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem2VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FOREX]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem3VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[LOCATOR]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem4VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[INVESTMENTS_N_CREDIT]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem5VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CREDIT_CARD_PAYMENTS]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem6VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[AGENCY_BANKING]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem7VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CONTACT_US]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);*/
			}else
			{
			/*	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem1VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BALANCE]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem2VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PAY_BILL]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem3VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[WESTERN_UNION]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem4VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PREPAID_CARDS]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem5VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FUNDS_TRANSFER]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem6VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PRODUCT_INFO]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, leftMenuItem7VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[HELP]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);*/
				}
		
		if(iconPlacement[1] == 1)
		{
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem1VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[SEND_MONEY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem2VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[RECEIVE_MONEY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem3VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[LOCATOR]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem4VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[TRANSACTION_HISTORY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem5VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BENEFICIARIES]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem6VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[INQUIRIES_N_FAQS]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem7VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[MY_ACCOUNT]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		}else if(iconPlacement[1] == 2)
		{
			/*gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem1VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CHEQUE_B00K_REQUEST]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem2VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FOREX]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem3VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[LOCATOR]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem4VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[INVESTMENTS_N_CREDIT]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem5VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CREDIT_CARD_PAYMENTS]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem6VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[AGENCY_BANKING]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem7VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CONTACT_US]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);*/
			}else
			{
			/*	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem1VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BALANCE]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem2VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PAY_BILL]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem3VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[WESTERN_UNION]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem4VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PREPAID_CARDS]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem5VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FUNDS_TRANSFER]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem6VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PRODUCT_INFO]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, menuItem7VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[HELP]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);*/
				}
	if(iconPlacement[2] == 1)
		{
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem1VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[SEND_MONEY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem2VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[RECEIVE_MONEY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem3VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[LOCATOR]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem4VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[TRANSACTION_HISTORY]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem5VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BENEFICIARIES]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem6VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[INQUIRIES_N_FAQS]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem7VertexBfr);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[MY_ACCOUNT]);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		}else if(iconPlacement[2] == 2)
		{
			/*gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem1VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CHEQUE_B00K_REQUEST]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem2VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FOREX]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem3VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[LOCATOR]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem4VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[INVESTMENTS_N_CREDIT]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem5VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CREDIT_CARD_PAYMENTS]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem6VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[AGENCY_BANKING]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem7VertexBfr);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[CONTACT_US]);
			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
					GL10.GL_UNSIGNED_SHORT, indexBuffer);*/
			}else
			{
			/*	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem1VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BALANCE]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem2VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PAY_BILL]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem3VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[WESTERN_UNION]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem4VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PREPAID_CARDS]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem5VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FUNDS_TRANSFER]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem6VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[PRODUCT_INFO]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, rightMenuItem7VertexBfr);
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBfr);
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[HELP]);
				gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
						GL10.GL_UNSIGNED_SHORT, indexBuffer);*/
				}
	
		// Disable the use of UV coordinates.
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Disable the use of textures.
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// Disable the color buffer.
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		// Disable face culling.
		//gl.glDisable(GL10.GL_CULL_FACE);
	
		
		// update rotations
		//sceneState.pyramidRot += 0.8f;
		//sceneState.cubeRot -= 0.5f;
		// get current millis
		//long currentMillis = System.currentTimeMillis();
		
		// update rotations
		/*if (lastMillis != 0) {
			long delta = currentMillis - lastMillis;
			sceneState.dx += sceneState.dxSpeed * delta;
			sceneState.dy += sceneState.dySpeed * delta;
			sceneState.dampenSpeed(delta);
		}*/
		sceneState.dampenSpeed();
		// update millis
		//lastMillis = currentMillis;
	}
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		int x = 0, y = 0;

		if(width < height){
			//x is initialized to 0;
			y = height/2 - width/2;

			//width remains the same;
			height = width;	
		}
	else if(width > height){
	x = width/2 - height/2;
	width = height;
	}
		xShift = x;
		yShift = y;

		// avoid division by zero
		if (height == 0) height = 1;
		// draw on the entire screen
		gl.glViewport(x, y, width, height);
		// setup projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 1.0f, 100.0f);
		screenWidth = width;
		screenHeight = height;
	}

	public MatrixGrabber getMatrixGrabber() {
		return matrixGrabber;
	}
	
	public int getScreenWidth(){
		return screenWidth;
	}
	public int getScreenHeight(){
		return screenHeight;
	}
	private void loadBitmaps(){
sendMoney = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_send_money);
receiveMoney = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_receive_money);
locator = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_locator);
transactionHistory = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_transaction_history);
beneficiaries = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_beneficiaries);
inquiriesNfaqs = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_inquiries);
myAccount = BitmapFactory.decodeResource(context.getResources(), R.drawable.mg_my_account);
}
	
	private void loadTextures(GL10 gl) {

		gl.glGenTextures(7, textures, 0);
		loadTexture(gl, textures[SEND_MONEY], sendMoney);
		loadTexture(gl, textures[RECEIVE_MONEY], receiveMoney);
		loadTexture(gl, textures[LOCATOR], locator);
		loadTexture(gl, textures[TRANSACTION_HISTORY], transactionHistory);	
		loadTexture(gl, textures[BENEFICIARIES], beneficiaries);
		loadTexture(gl, textures[INQUIRIES_N_FAQS], inquiriesNfaqs);
		loadTexture(gl, textures[MY_ACCOUNT], myAccount);
	}
	private void loadTexture(GL10 gl, int textureId, Bitmap bmp) {
	 
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
	 
		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR );
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR );
	 
		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		//gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
	 
		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
		bmp.recycle();
	}
	
	public byte testIntersection(float rayStart[], float rayVector[]) {
		currentT = 1.1f;
		selection = 0;

		byte hit = 0;
		float a, b, c, d, e, f, g, h, i, j, k, l;
		//indices 0, 3, 1, 3, 2, 1	
		a = musicCoords[0] - musicCoords[9]; 
		b = musicCoords[1] - musicCoords[10]; 
		c = musicCoords[2] - musicCoords[11]; 
		d = musicCoords[0] - musicCoords[3]; 
		e = musicCoords[1] - musicCoords[4]; 
		f = musicCoords[2] - musicCoords[5];
		g = rayVector[0];
		h = rayVector[1];
		i = rayVector[2];
		j = musicCoords[0] - rayStart[0];
		k = musicCoords[1] - rayStart[1];
		l = musicCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			currentT = t;
			selection = 1;
		}	
		a = musicCoords[9] - musicCoords[6]; 
		b = musicCoords[10] - musicCoords[7]; 
		c = musicCoords[11] - musicCoords[8]; 
		d = musicCoords[9] - musicCoords[3]; 
		e = musicCoords[10] - musicCoords[4]; 
		f = musicCoords[11] - musicCoords[5];
		j = musicCoords[9] - rayStart[0];
		k = musicCoords[10] - rayStart[1];
		l = musicCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			currentT = t;
			selection = 1;
		}
		hit = 0;
		a = musicBackCoords[0] - musicBackCoords[9]; 
		b = musicBackCoords[1] - musicBackCoords[10]; 
		c = musicBackCoords[2] - musicBackCoords[11]; 
		d = musicBackCoords[0] - musicBackCoords[3]; 
		e = musicBackCoords[1] - musicBackCoords[4]; 
		f = musicBackCoords[2] - musicBackCoords[5];
		j = musicBackCoords[0] - rayStart[0];
		k = musicBackCoords[1] - rayStart[1];
		l = musicBackCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 1;
			}
		}
		a = musicBackCoords[9] - musicBackCoords[6]; 
		b = musicBackCoords[10] - musicBackCoords[7]; 
		c = musicBackCoords[11] - musicBackCoords[8]; 
		d = musicBackCoords[9] - musicBackCoords[3]; 
		e = musicBackCoords[10] - musicBackCoords[4]; 
		f = musicBackCoords[11] - musicBackCoords[5];
		j = musicBackCoords[9] - rayStart[0];
		k = musicBackCoords[10] - rayStart[1];
		l = musicBackCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 1;
			}
		}
		
		hit = 0;
		a = bankCoords[0] - bankCoords[9]; 
		b = bankCoords[1] - bankCoords[10]; 
		c = bankCoords[2] - bankCoords[11]; 
		d = bankCoords[0] - bankCoords[3]; 
		e = bankCoords[1] - bankCoords[4]; 
		f = bankCoords[2] - bankCoords[5];
		j = bankCoords[0] - rayStart[0];
		k = bankCoords[1] - rayStart[1];
		l = bankCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 2;
			}
		}
		a = bankCoords[9] - bankCoords[6]; 
		b = bankCoords[10] - bankCoords[7]; 
		c = bankCoords[11] - bankCoords[8]; 
		d = bankCoords[9] - bankCoords[3]; 
		e = bankCoords[10] - bankCoords[4]; 
		f = bankCoords[11] - bankCoords[5];
		j = bankCoords[9] - rayStart[0];
		k = bankCoords[10] - rayStart[1];
		l = bankCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 2;
			}
		}
		
		hit = 0;
		a = bankBackCoords[0] - bankBackCoords[9]; 
		b = bankBackCoords[1] - bankBackCoords[10]; 
		c = bankBackCoords[2] - bankBackCoords[11]; 
		d = bankBackCoords[0] - bankBackCoords[3]; 
		e = bankBackCoords[1] - bankBackCoords[4]; 
		f = bankBackCoords[2] - bankBackCoords[5];
		j = bankBackCoords[0] - rayStart[0];
		k = bankBackCoords[1] - rayStart[1];
		l = bankBackCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 2;
			}
		}
		a = bankBackCoords[9] - bankBackCoords[6]; 
		b = bankBackCoords[10] - bankBackCoords[7]; 
		c = bankBackCoords[11] - bankBackCoords[8]; 
		d = bankBackCoords[9] - bankBackCoords[3]; 
		e = bankBackCoords[10] - bankBackCoords[4]; 
		f = bankBackCoords[11] - bankBackCoords[5];
		j = bankBackCoords[9] - rayStart[0];
		k = bankBackCoords[10] - rayStart[1];
		l = bankBackCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 2;
			}
		}
		
		hit = 0;
		a = infoCoords[0] - infoCoords[9]; 
		b = infoCoords[1] - infoCoords[10]; 
		c = infoCoords[2] - infoCoords[11]; 
		d = infoCoords[0] - infoCoords[3]; 
		e = infoCoords[1] - infoCoords[4]; 
		f = infoCoords[2] - infoCoords[5];
		j = infoCoords[0] - rayStart[0];
		k = infoCoords[1] - rayStart[1];
		l = infoCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 3;
			}
		}
		a = infoCoords[9] - infoCoords[6]; 
		b = infoCoords[10] - infoCoords[7]; 
		c = infoCoords[11] - infoCoords[8]; 
		d = infoCoords[9] - infoCoords[3]; 
		e = infoCoords[10] - infoCoords[4]; 
		f = infoCoords[11] - infoCoords[5];
		j = infoCoords[9] - rayStart[0];
		k = infoCoords[10] - rayStart[1];
		l = infoCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 3;
			}
		}
		
		hit = 0;
		a = infoBackCoords[0] - infoBackCoords[9]; 
		b = infoBackCoords[1] - infoBackCoords[10]; 
		c = infoBackCoords[2] - infoBackCoords[11]; 
		d = infoBackCoords[0] - infoBackCoords[3]; 
		e = infoBackCoords[1] - infoBackCoords[4]; 
		f = infoBackCoords[2] - infoBackCoords[5];
		j = infoBackCoords[0] - rayStart[0];
		k = infoBackCoords[1] - rayStart[1];
		l = infoBackCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 3;
			}
		}
		a = infoBackCoords[9] - infoBackCoords[6]; 
		b = infoBackCoords[10] - infoBackCoords[7]; 
		c = infoBackCoords[11] - infoBackCoords[8]; 
		d = infoBackCoords[9] - infoBackCoords[3]; 
		e = infoBackCoords[10] - infoBackCoords[4]; 
		f = infoBackCoords[11] - infoBackCoords[5];
		j = infoBackCoords[9] - rayStart[0];
		k = infoBackCoords[10] - rayStart[1];
		l = infoBackCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
		if(hit > 0){
			if(currentT > t){
				currentT = t;
				selection = 3;
			}
		}
		return selection;
	}
	public boolean testTriangleIntersection(float a, float b, float c, float d, float e, float f,
	float g, float h, float i, float j, float k, float l) {
		//TODO optimize
		float eXi_hXf = e*i-h*f;
		float gXf_dXi = g*f-d*i;
		float dXh_eXg = d*h-e*g;
		float aXk_jXb = a*k-j*b;
		float jXc_aXl = j*c-a*l;
		float bXl_kXc = b*l-k*c;
		
		float beta, rho, det;
		det  = a*(eXi_hXf)+b*(gXf_dXi)+c*(dXh_eXg);
		t = -(f*(aXk_jXb)+e*(jXc_aXl)+d*(bXl_kXc))/det;
		
		if(t < 0 || t > 1){
			return false;
		}
		rho = (i*(aXk_jXb)+h*(jXc_aXl)+g*(bXl_kXc))/det;		
		if(rho < 0 || rho > 1){
			return false;
		}
		beta = (j*(eXi_hXf)+k*(gXf_dXi)+l*(dXh_eXg))/det;
		if(beta < 0 || beta > 1-rho){
			return false;
		}

		return true;
	}
	public void swipeIcons(boolean right){
		int temp[] = new int[3];
		temp[0] = iconPlacement[0];
		temp[1] = iconPlacement[1];
		temp[2] = iconPlacement[2];
		if(right){
				iconPlacement[0] = temp[2];
				iconPlacement[1] = temp[0];
				iconPlacement[2] = temp[1];
		}else{
			iconPlacement[0] = temp[1];
			iconPlacement[1] = temp[2];
			iconPlacement[2] = temp[0];			
		}
	}
	
	public byte testCenterIconHit(float rayStart[], float rayVector[]) {

		byte hit = 0;
		float a, b, c, d, e, f, g, h, i, j, k, l;
		//indices 0, 3, 1, 3, 2, 1	
		a = bankCoords[0] - bankCoords[9]; 
		b = bankCoords[1] - bankCoords[10]; 
		c = bankCoords[2] - bankCoords[11]; 
		d = bankCoords[0] - bankCoords[3]; 
		e = bankCoords[1] - bankCoords[4]; 
		f = bankCoords[2] - bankCoords[5];
		g = rayVector[0];
		h = rayVector[1];
		i = rayVector[2];
		j = bankCoords[0] - rayStart[0];
		k = bankCoords[1] - rayStart[1];
		l = bankCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
	
		a = bankCoords[9] - bankCoords[6]; 
		b = bankCoords[10] - bankCoords[7]; 
		c = bankCoords[11] - bankCoords[8]; 
		d = bankCoords[9] - bankCoords[3]; 
		e = bankCoords[10] - bankCoords[4]; 
		f = bankCoords[11] - bankCoords[5];
		j = bankCoords[9] - rayStart[0];
		k = bankCoords[10] - rayStart[1];
		l = bankCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			hit++;
		}
	
		return hit;
	}
	public byte testMenuHit(float rayStart[], float rayVector[]) {
		if(testMenuHit(rayStart, rayVector, menuItem1Coords, (byte)1) > 0){
		return 1;	
		}
		if(testMenuHit(rayStart, rayVector, menuItem2Coords, (byte)2) > 0){
			return 2;	
			}
		if(testMenuHit(rayStart, rayVector, menuItem3Coords, (byte)3) > 0){
			return 3;	
			}
		if(testMenuHit(rayStart, rayVector, menuItem4Coords, (byte)4) > 0){
			return 4;	
			}
		if(testMenuHit(rayStart, rayVector, menuItem5Coords, (byte)5) > 0){
			return 5;	
			}
		if(testMenuHit(rayStart, rayVector, menuItem6Coords, (byte)6) > 0){
			return 6;	
			}
		if(testMenuHit(rayStart, rayVector, menuItem7Coords, (byte)7) > 0){
			return 7;	
			}
		return 0;	
	}
	
	public byte testMenuHit(float rayStart[], float rayVector[], 
			float menuItemCoords[], byte menu) {

		byte hit = 0;
		float a, b, c, d, e, f, g, h, i, j, k, l;
		//indices 0, 3, 1, 3, 2, 1	
		a = menuItemCoords[0] - menuItemCoords[9]; 
		b = menuItemCoords[1] - menuItemCoords[10]; 
		c = menuItemCoords[2] - menuItemCoords[11]; 
		d = menuItemCoords[0] - menuItemCoords[3]; 
		e = menuItemCoords[1] - menuItemCoords[4]; 
		f = menuItemCoords[2] - menuItemCoords[5];
		g = rayVector[0];
		h = rayVector[1];
		i = rayVector[2];
		j = menuItemCoords[0] - rayStart[0];
		k = menuItemCoords[1] - rayStart[1];
		l = menuItemCoords[2] - rayStart[2];
		
		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			return menu;
		}
	
		a = menuItemCoords[9] - menuItemCoords[6]; 
		b = menuItemCoords[10] - menuItemCoords[7]; 
		c = menuItemCoords[11] - menuItemCoords[8]; 
		d = menuItemCoords[9] - menuItemCoords[3]; 
		e = menuItemCoords[10] - menuItemCoords[4]; 
		f = menuItemCoords[11] - menuItemCoords[5];
		j = menuItemCoords[9] - rayStart[0];
		k = menuItemCoords[10] - rayStart[1];
		l = menuItemCoords[11] - rayStart[2];

		if(testTriangleIntersection(a, b, c, d, e, f, g, h, i, j, k, l)){
			return menu;
		}
	
		return hit;
	}
	
	public int getSelection(){
		return iconPlacement[1];
	}
	public int getYShift(){
		return yShift;
	}
	public int getXShift(){
		return xShift;
	}
	private static float rotateXalongZ(float x, float y, double theta){
	return (x*(float)Math.cos(theta)) - (y*(float)Math.sin(theta));
	}
	private static float rotateYalongZ(float x, float y, double theta){
		return (x*(float)Math.sin(theta)) + (y*(float)Math.cos(theta));
		}
	private void initMenuItems(){
		currentCenter = new float[] {0.0f, 0.6f};
		 menuItem1Coords = new float[] {
					currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
					currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
		};
		 //float tempCenter[] = currentCenter;
		 currentCenter[0] = rotateXalongZ(centerX, centerY, Y_AXIS_THETA);
		 currentCenter[1] = rotateYalongZ(centerX, centerY, Y_AXIS_THETA);
		
		 menuItem2Coords = new float[] {
			currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
			currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
			currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
			currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
		};
		 
		 float tempCenter2[] = currentCenter;

		 currentCenter[0] = rotateXalongZ(centerX, centerY, Y_AXIS_THETA*2);
		 currentCenter[1] = rotateYalongZ(centerX, centerY, Y_AXIS_THETA*2);
		 
		 menuItem3Coords = new float[] {
					currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
					currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
		};
		
		 //tempCenter = currentCenter;
		 currentCenter[0] = rotateXalongZ(centerX, centerY, Y_AXIS_THETA*3);
		 currentCenter[1] = rotateYalongZ(centerX, centerY, Y_AXIS_THETA*3);
		 
		menuItem4Coords = new float[] {
				currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
				currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
				currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
				currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
};
		 //tempCenter = currentCenter;
		currentCenter[0] = rotateXalongZ(centerX, centerY, Y_AXIS_THETA*4);
		 currentCenter[1] = rotateYalongZ(centerX, centerY, Y_AXIS_THETA*4);
		 
		  menuItem5Coords = new float[] {
					currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
					currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
		};
		// tempCenter = currentCenter;
		 currentCenter[0] = rotateXalongZ(centerX, centerY, Y_AXIS_THETA*5);
		 currentCenter[1] = rotateYalongZ(centerX, centerY, Y_AXIS_THETA*5);
		 
		 menuItem6Coords = new float[] {
					currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
					currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
		};
	
		// tempCenter = currentCenter;
		 currentCenter[0] = rotateXalongZ(centerX, centerY, Y_AXIS_THETA*6);
		 currentCenter[1] = rotateYalongZ(centerX, centerY, Y_AXIS_THETA*6);
		 
		 menuItem7Coords = new float[] {
					currentCenter[0]-RADIUS,  currentCenter[1]-RADIUS,  1,
					currentCenter[0]-RADIUS, currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]+RADIUS,  1,
					currentCenter[0]+RADIUS,  currentCenter[1]-RADIUS,  1
		};
		 
		 float dx = rotateYalongZ(menuItem6Coords[2], menuItem6Coords[0], -Z_AXIS_THETA) + 0.89f + 0.305f;
		 float dz = 0.45f;
		 
		 leftMenuItem1Coords = new float[] {
rotateYalongZ(menuItem1Coords[2], menuItem1Coords[0], -Z_AXIS_THETA)-dx,  menuItem1Coords[1],  rotateXalongZ(menuItem1Coords[2], menuItem1Coords[0], -Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem1Coords[5], menuItem1Coords[3], -Z_AXIS_THETA)-dx,  menuItem1Coords[4],  rotateXalongZ(menuItem1Coords[5], menuItem1Coords[3], -Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem1Coords[8], menuItem1Coords[6], -Z_AXIS_THETA)-dx,  menuItem1Coords[7],  rotateXalongZ(menuItem1Coords[8], menuItem1Coords[6], -Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem1Coords[11], menuItem1Coords[9], -Z_AXIS_THETA)-dx,  menuItem1Coords[10],  rotateXalongZ(menuItem1Coords[11], menuItem1Coords[9], -Z_AXIS_THETA)-dz
		};
		 
leftMenuItem2Coords = new float[] {
				 rotateYalongZ(menuItem2Coords[2], menuItem2Coords[0], -Z_AXIS_THETA)-dx,  menuItem2Coords[1],  rotateXalongZ(menuItem2Coords[2], menuItem2Coords[0], -Z_AXIS_THETA)-dz,
				 rotateYalongZ(menuItem2Coords[5], menuItem2Coords[3], -Z_AXIS_THETA)-dx,  menuItem2Coords[4],  rotateXalongZ(menuItem2Coords[5], menuItem2Coords[3], -Z_AXIS_THETA)-dz,
				 rotateYalongZ(menuItem2Coords[8], menuItem2Coords[6], -Z_AXIS_THETA)-dx,  menuItem2Coords[7],  rotateXalongZ(menuItem2Coords[8], menuItem2Coords[6], -Z_AXIS_THETA)-dz,
				 rotateYalongZ(menuItem2Coords[11], menuItem2Coords[9], -Z_AXIS_THETA)-dx,  menuItem2Coords[10],  rotateXalongZ(menuItem2Coords[11], menuItem2Coords[9], -Z_AXIS_THETA)-dz
				 		};

leftMenuItem3Coords = new float[] {
		 rotateYalongZ(menuItem3Coords[2], menuItem3Coords[0], -Z_AXIS_THETA)-dx,  menuItem3Coords[1],  rotateXalongZ(menuItem3Coords[2], menuItem3Coords[0], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem3Coords[5], menuItem3Coords[3], -Z_AXIS_THETA)-dx,  menuItem3Coords[4],  rotateXalongZ(menuItem3Coords[5], menuItem3Coords[3], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem3Coords[8], menuItem3Coords[6], -Z_AXIS_THETA)-dx,  menuItem3Coords[7],  rotateXalongZ(menuItem3Coords[8], menuItem3Coords[6], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem3Coords[11], menuItem3Coords[9], -Z_AXIS_THETA)-dx,  menuItem3Coords[10],  rotateXalongZ(menuItem3Coords[11], menuItem3Coords[9], -Z_AXIS_THETA)-dz
		 		};

leftMenuItem4Coords = new float[] {
		 rotateYalongZ(menuItem4Coords[2], menuItem4Coords[0], -Z_AXIS_THETA)-dx,  menuItem4Coords[1],  rotateXalongZ(menuItem4Coords[2], menuItem4Coords[0], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem4Coords[5], menuItem4Coords[3], -Z_AXIS_THETA)-dx,  menuItem4Coords[4],  rotateXalongZ(menuItem4Coords[5], menuItem4Coords[3], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem4Coords[8], menuItem4Coords[6], -Z_AXIS_THETA)-dx,  menuItem4Coords[7],  rotateXalongZ(menuItem4Coords[8], menuItem4Coords[6], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem4Coords[11], menuItem4Coords[9], -Z_AXIS_THETA)-dx,  menuItem4Coords[10],  rotateXalongZ(menuItem4Coords[11], menuItem4Coords[9], -Z_AXIS_THETA)-dz
		 		};

leftMenuItem5Coords = new float[] {
		 rotateYalongZ(menuItem5Coords[2], menuItem5Coords[0], -Z_AXIS_THETA)-dx,  menuItem5Coords[1],  rotateXalongZ(menuItem5Coords[2], menuItem5Coords[0], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem5Coords[5], menuItem5Coords[3], -Z_AXIS_THETA)-dx,  menuItem5Coords[4],  rotateXalongZ(menuItem5Coords[5], menuItem5Coords[3], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem5Coords[8], menuItem5Coords[6], -Z_AXIS_THETA)-dx,  menuItem5Coords[7],  rotateXalongZ(menuItem5Coords[8], menuItem5Coords[6], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem5Coords[11], menuItem5Coords[9], -Z_AXIS_THETA)-dx,  menuItem5Coords[10],  rotateXalongZ(menuItem5Coords[11], menuItem5Coords[9], -Z_AXIS_THETA)-dz
		 		};

leftMenuItem6Coords = new float[] {
		 rotateYalongZ(menuItem6Coords[2], menuItem6Coords[0], -Z_AXIS_THETA)-dx,  menuItem6Coords[1],  rotateXalongZ(menuItem6Coords[2], menuItem6Coords[0], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem6Coords[5], menuItem6Coords[3], -Z_AXIS_THETA)-dx,  menuItem6Coords[4],  rotateXalongZ(menuItem6Coords[5], menuItem6Coords[3], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem6Coords[8], menuItem6Coords[6], -Z_AXIS_THETA)-dx,  menuItem6Coords[7],  rotateXalongZ(menuItem6Coords[8], menuItem6Coords[6], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem6Coords[11], menuItem6Coords[9], -Z_AXIS_THETA)-dx,  menuItem6Coords[10],  rotateXalongZ(menuItem6Coords[11], menuItem6Coords[9], -Z_AXIS_THETA)-dz
		 		};

leftMenuItem7Coords = new float[] {
		 rotateYalongZ(menuItem7Coords[2], menuItem7Coords[0], -Z_AXIS_THETA)-dx,  menuItem7Coords[1],  rotateXalongZ(menuItem7Coords[2], menuItem7Coords[0], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem7Coords[5], menuItem7Coords[3], -Z_AXIS_THETA)-dx,  menuItem7Coords[4],  rotateXalongZ(menuItem7Coords[5], menuItem7Coords[3], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem7Coords[8], menuItem7Coords[6], -Z_AXIS_THETA)-dx,  menuItem7Coords[7],  rotateXalongZ(menuItem7Coords[8], menuItem7Coords[6], -Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem7Coords[11], menuItem7Coords[9], -Z_AXIS_THETA)-dx,  menuItem7Coords[10],  rotateXalongZ(menuItem7Coords[11], menuItem7Coords[9], -Z_AXIS_THETA)-dz
		 		};

dx = 0.89f -0.005f - rotateYalongZ(menuItem3Coords[2], menuItem3Coords[0], Z_AXIS_THETA);

rightMenuItem1Coords = new float[] {
rotateYalongZ(menuItem1Coords[2], menuItem1Coords[0], Z_AXIS_THETA)+dx,  menuItem1Coords[1],  rotateXalongZ(menuItem1Coords[2], menuItem1Coords[0], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem1Coords[5], menuItem1Coords[3], Z_AXIS_THETA)+dx,  menuItem1Coords[4],  rotateXalongZ(menuItem1Coords[5], menuItem1Coords[3], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem1Coords[8], menuItem1Coords[6], Z_AXIS_THETA)+dx,  menuItem1Coords[7],  rotateXalongZ(menuItem1Coords[8], menuItem1Coords[6], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem1Coords[11], menuItem1Coords[9], Z_AXIS_THETA)+dx,  menuItem1Coords[10],  rotateXalongZ(menuItem1Coords[11], menuItem1Coords[9], Z_AXIS_THETA)-dz
};

rightMenuItem2Coords = new float[] {
		 rotateYalongZ(menuItem2Coords[2], menuItem2Coords[0], Z_AXIS_THETA)+dx,  menuItem2Coords[1],  rotateXalongZ(menuItem2Coords[2], menuItem2Coords[0], Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem2Coords[5], menuItem2Coords[3], Z_AXIS_THETA)+dx,  menuItem2Coords[4],  rotateXalongZ(menuItem2Coords[5], menuItem2Coords[3], Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem2Coords[8], menuItem2Coords[6], Z_AXIS_THETA)+dx,  menuItem2Coords[7],  rotateXalongZ(menuItem2Coords[8], menuItem2Coords[6], Z_AXIS_THETA)-dz,
		 rotateYalongZ(menuItem2Coords[11], menuItem2Coords[9], Z_AXIS_THETA)+dx,  menuItem2Coords[10],  rotateXalongZ(menuItem2Coords[11], menuItem2Coords[9], Z_AXIS_THETA)-dz
		 		};

rightMenuItem3Coords = new float[] {
rotateYalongZ(menuItem3Coords[2], menuItem3Coords[0], Z_AXIS_THETA)+dx,  menuItem3Coords[1],  rotateXalongZ(menuItem3Coords[2], menuItem3Coords[0], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem3Coords[5], menuItem3Coords[3], Z_AXIS_THETA)+dx,  menuItem3Coords[4],  rotateXalongZ(menuItem3Coords[5], menuItem3Coords[3], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem3Coords[8], menuItem3Coords[6], Z_AXIS_THETA)+dx,  menuItem3Coords[7],  rotateXalongZ(menuItem3Coords[8], menuItem3Coords[6], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem3Coords[11], menuItem3Coords[9], Z_AXIS_THETA)+dx,  menuItem3Coords[10],  rotateXalongZ(menuItem3Coords[11], menuItem3Coords[9], Z_AXIS_THETA)-dz
		};

rightMenuItem4Coords = new float[] {
rotateYalongZ(menuItem4Coords[2], menuItem4Coords[0], Z_AXIS_THETA)+dx,  menuItem4Coords[1],  rotateXalongZ(menuItem4Coords[2], menuItem4Coords[0], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem4Coords[5], menuItem4Coords[3], Z_AXIS_THETA)+dx,  menuItem4Coords[4],  rotateXalongZ(menuItem4Coords[5], menuItem4Coords[3], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem4Coords[8], menuItem4Coords[6], Z_AXIS_THETA)+dx,  menuItem4Coords[7],  rotateXalongZ(menuItem4Coords[8], menuItem4Coords[6], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem4Coords[11], menuItem4Coords[9], Z_AXIS_THETA)+dx,  menuItem4Coords[10],  rotateXalongZ(menuItem4Coords[11], menuItem4Coords[9], Z_AXIS_THETA)-dz
		};

rightMenuItem5Coords = new float[] {
rotateYalongZ(menuItem5Coords[2], menuItem5Coords[0], Z_AXIS_THETA)+dx,  menuItem5Coords[1],  rotateXalongZ(menuItem5Coords[2], menuItem5Coords[0], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem5Coords[5], menuItem5Coords[3], Z_AXIS_THETA)+dx,  menuItem5Coords[4],  rotateXalongZ(menuItem5Coords[5], menuItem5Coords[3], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem5Coords[8], menuItem5Coords[6], Z_AXIS_THETA)+dx,  menuItem5Coords[7],  rotateXalongZ(menuItem5Coords[8], menuItem5Coords[6], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem5Coords[11], menuItem5Coords[9], Z_AXIS_THETA)+dx,  menuItem5Coords[10],  rotateXalongZ(menuItem5Coords[11], menuItem5Coords[9], Z_AXIS_THETA)-dz
		};

rightMenuItem6Coords = new float[] {
rotateYalongZ(menuItem6Coords[2], menuItem6Coords[0], Z_AXIS_THETA)+dx,  menuItem6Coords[1],  rotateXalongZ(menuItem6Coords[2], menuItem6Coords[0], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem6Coords[5], menuItem6Coords[3], Z_AXIS_THETA)+dx,  menuItem6Coords[4],  rotateXalongZ(menuItem6Coords[5], menuItem6Coords[3], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem6Coords[8], menuItem6Coords[6], Z_AXIS_THETA)+dx,  menuItem6Coords[7],  rotateXalongZ(menuItem6Coords[8], menuItem6Coords[6], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem6Coords[11], menuItem6Coords[9], Z_AXIS_THETA)+dx,  menuItem6Coords[10],  rotateXalongZ(menuItem6Coords[11], menuItem6Coords[9], Z_AXIS_THETA)-dz
		};

rightMenuItem7Coords = new float[] {
rotateYalongZ(menuItem7Coords[2], menuItem7Coords[0], Z_AXIS_THETA)+dx,  menuItem7Coords[1],  rotateXalongZ(menuItem7Coords[2], menuItem7Coords[0], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem7Coords[5], menuItem7Coords[3], Z_AXIS_THETA)+dx,  menuItem7Coords[4],  rotateXalongZ(menuItem7Coords[5], menuItem7Coords[3], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem7Coords[8], menuItem7Coords[6], Z_AXIS_THETA)+dx,  menuItem7Coords[7],  rotateXalongZ(menuItem7Coords[8], menuItem7Coords[6], Z_AXIS_THETA)-dz,
rotateYalongZ(menuItem7Coords[11], menuItem7Coords[9], Z_AXIS_THETA)+dx,  menuItem7Coords[10],  rotateXalongZ(menuItem7Coords[11], menuItem7Coords[9], Z_AXIS_THETA)-dz
		};
	}
}