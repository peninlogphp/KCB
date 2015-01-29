package lipuka.android.view;

import kcb.android.CFChome;
import kcb.android.Main;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;


import com.bornander.math.Vector2D;


public class InfoGrafixImage implements InfoGrafixObject{

	private final Bitmap originalBitmap;
private Bitmap bitmap;
	private int width;
	private int height;
	private Vector2D position = new Vector2D();
	private float scale = 1;
	private float angle = 0;

	// Debug helpers to draw lines between the two touch points
	private Vector2D vca = null;
	private Vector2D vcb = null;
	private Vector2D vpa = null;
	private Vector2D vpb = null;
	CFChome infoGrafixHome;
	float actualAngle = 0.0f;
	int centerX = 0;
	int centerY = 0;
	int actionBarHeight;
	double newLeft, newTop, newRight, newBottom;
	double distX1, distY1, distX2, distY2, distX3, distY3, distX4, distY4 = 0;
Point corner1 = new Point();
Point corner2 = new Point();
Point corner3 = new Point();
Point corner4 = new Point();
private int originalWidth;
private int originalHeight;
int originalCenterX = 0;
int originalCenterY = 0;
	public InfoGrafixImage(Context context, Bitmap bitmap, int centerX, int centerY,
			int width, int height) {
		infoGrafixHome = (CFChome)context;
		this.bitmap = bitmap;
		originalBitmap = this.bitmap;
		//this.width = bitmap.getWidth();
		//this.height = bitmap.getHeight();
		this.width = width;
		this.height = height;
		originalWidth = width;
		originalHeight = height;

this.centerX =  centerX;
this.centerY = centerY;
originalCenterX = centerX;
originalCenterY = centerY;

	distX1 = distX2 = distX3 = distX4 = (float)(width/2);
	distY1 = distY2 = distY3 = distY4 = (float)(height/2);
	
	newLeft = centerX - Math.max(distX1, distX4);
newTop = centerY - Math.max(distY1, distY2);
newRight = centerX + Math.max(distX2, distX3);
newBottom = centerY + Math.max(distY3, distY4);	

corner1.x = (int)centerX - (int)distX1;
corner1.y = (int)centerY - (int)distY1;
corner2.x = (int)centerX + (int)distX2;
corner2.y = (int)centerY - (int)distY2;
corner3.x = (int)centerX + (int)distX3;
corner3.y = (int)centerY + (int)distY3;
corner4.x = (int)centerX - (int)distX4;
corner4.y = (int)centerY + (int)distY4;
	
	}


	private static float getDegreesFromRadians(float angle) {
		return (float)(angle * 180.0 / Math.PI);
	}

	public void draw(Canvas canvas, Matrix transform, Paint paint) {

		transform.reset();
		canvas.save();
width = (int)(originalWidth * scale);
		height = (int)(originalHeight * scale);
		//float dx =  (float)(width/2);
		//float dy =  (float)(height/2);
		float dx =  centerX;
		float dy =  0.0f;
		
		centerX = originalCenterX + (int)position.getX();
		centerY = originalCenterY + (int)position.getY();
		
		//transform.postTranslate(-dx, 0);
		//transform.postRotate(getDegreesFromRadians(angle));
		transform.setRotate(getDegreesFromRadians(-angle), 60, 60);	
		//transform.postTranslate(dx, 0);

		canvas.rotate(getDegreesFromRadians(angle),  0, centerY);
canvas.translate(centerX - (float)(width/2), centerY - (float)(height/2));
		calculateRotationBounds();

		Bitmap bmp = Bitmap.createScaledBitmap(bitmap, width, height, false);
	canvas.drawBitmap(bmp, transform, paint);
	canvas.restore();

	Log.d(Main.TAG, "drawn image");
	Log.d(Main.TAG, "position x: "+position.getX());
	Log.d(Main.TAG, "position y: "+position.getY());
	Log.d(Main.TAG, "angle: "+angle);
	Log.d(Main.TAG, "scale: "+scale);
	}

public float getAngle(){
	return angle;
}
public void setAngle(float angle){
	this.angle = angle;
}
public float getScale(){
	return scale;
}
public void setScale(float scale){
	this.scale = scale;
}
public Vector2D getPosition(){
	return position;
}
public void setPosition(Vector2D position){
	this.position = position;
}
public int getCenterX(){
	return centerX;
}
public int getCenterY(){
	return centerY;
}
	public void calculateRotationBounds(){
		//float w = centerX - (float)(width/2);
		//float h = centerY - (float)(height/2);
		float w = (float)(width/2);
		float h = (float)(height/2);
		double L = Math.sqrt((w*w)+(h*h));
		float testAngle = angle * 180.0f / (float) Math.PI;
		//testAngle = testAngle % 360; 
		//distX1 = distY1 = distX2 = distY2 = distX3 = distY3 = distX4 = distY4 = 0;
		double theta = 0;
		if(testAngle == 0){
			distX1 = distX2 = distX3 = distX4 = w;
			distY1 = distY2 = distY3 = distY4 = h;
		}else if(testAngle > 0 && testAngle <= 45){
			theta = 45 - testAngle;
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);
		}else if(testAngle > 45 && testAngle <= 135){
			theta = 135 - testAngle;
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);
		}else if(testAngle > 135 && testAngle <= 225){
			theta = 225 - testAngle;
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);				
		}else if(testAngle > 225 && testAngle <= 315){
			theta = 315 - testAngle;
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);								
		}else if(testAngle > 315 && testAngle <= 360){
			theta = testAngle - 315;
			distX1=  L * Math.cos(theta);
			distY1=  L * Math.sin(theta);
			distX2=  L * Math.sin(theta);
			distY2=  L * Math.cos(theta);
			distX3=  L * Math.cos(theta);
			distY3=  L * Math.sin(theta);
			distX4=  L * Math.sin(theta);
			distY4=  L * Math.cos(theta);
		}else if(testAngle >= -45 && testAngle < 0){
			theta = 45 + testAngle;
			distX1=  L * Math.cos(theta);
			distY1=  L * Math.sin(theta);
			distX2=  L * Math.sin(theta);
			distY2=  L * Math.cos(theta);
			distX3=  L * Math.cos(theta);
			distY3=  L * Math.sin(theta);
			distX4=  L * Math.sin(theta);
			distY4=  L * Math.cos(theta);
		}else if(testAngle >= -135 && testAngle < -45){
			theta = Math.abs(testAngle) - 45;
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);	
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);							
		}else if(testAngle >= -225 && testAngle < -135){
			theta = Math.abs(testAngle) - 135;
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);	
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);							
		}else if(testAngle >= -315 && testAngle < -225){
			theta = Math.abs(testAngle) - 225;
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);	
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);							
		}else if(testAngle >= -360 && testAngle < -315){
			theta = Math.abs(testAngle) - 315;			
			distX1=  L * Math.sin(theta);
			distY1=  L * Math.cos(theta);
			distX4=  L * Math.cos(theta);
			distY4=  L * Math.sin(theta);	
			distX3=  L * Math.sin(theta);
			distY3=  L * Math.cos(theta);	
			distX2=  L * Math.cos(theta);
			distY2=  L * Math.sin(theta);
		}
		newLeft = centerX - Math.max(distX1, distX4);
		newTop = centerY - Math.max(distY1, distY2);
		newRight = centerX + Math.max(distX2, distX3);
		newBottom = centerY + Math.max(distY3, distY4);	
		
		corner1.x = (int)centerX - (int)distX1;
		corner1.y = (int)centerY - (int)distY1;
		corner2.x = (int)centerX + (int)distX2;
		corner2.y = (int)centerY - (int)distY2;
		corner3.x = (int)centerX + (int)distX3;
		corner3.y = (int)centerY + (int)distY3;
		corner4.x = (int)centerX - (int)distX4;
		corner4.y = (int)centerY + (int)distY4;
		
	}
	
	public boolean hitTest(float scrnX, float scrnY) {	
		Point touchPoint = new Point((int)scrnX, (int)scrnY);
		Log.d(Main.TAG, "scrnX: "+scrnX); 
		Log.d(Main.TAG, "scrnY: "+scrnY); 
		Log.d(Main.TAG, "corner1.x: "+corner1.x); 
		Log.d(Main.TAG, "corner1.y: "+corner1.y); 
		Log.d(Main.TAG, "corner2.x: "+corner2.x); 
		Log.d(Main.TAG, "corner2.y: "+corner2.y); 
		Log.d(Main.TAG, "corner3.x: "+corner3.x); 
		Log.d(Main.TAG, "corner3.y: "+corner3.y); 
		Log.d(Main.TAG, "corner4.x: "+corner4.x); 
		Log.d(Main.TAG, "corner4.y: "+corner4.y); 
		Log.d(Main.TAG, "centerX: "+centerX); 
		Log.d(Main.TAG, "centerY: "+centerY); 

if (checkPointInTriangleBarycentric(corner1, corner3, corner4, touchPoint) ||
	checkPointInTriangleBarycentric(corner1, corner2, corner3, touchPoint)) {
	        return true;
	     }
return false;
		
	}
	private boolean checkPointInTriangleBarycentric(Point pt1, Point pt2, Point pt3, Point p) 
    {
        Point v1 = new Point(pt2.x - pt1.x, pt2.y - pt1.y);
        Point v2 = new Point(pt3.x - pt1.x, pt3.y - pt1.y);
        
        Point q = new Point(p.x - pt1.x, p.y - pt1.y);
        
        float s = (float)crossProduct(q, v2) / crossProduct(v1, v2);
        float t = (float)crossProduct(v1, q) / crossProduct(v1, v2);
         
        if ( (s >= 0) && (t >= 0) && (s + t <= 1))
        {
            /* point is inside the triangle */
            return true;
        }
        else
        {
            return false;
        }
    }
	private int crossProduct(Point p1, Point p2)
    {
        return (p1.x * p2.y - p1.y * p2.x);
    }
	public Bitmap getBitmap()
    {
      return originalBitmap;
    }
	public void setBitmap(Bitmap bitmap)
    {
      this.bitmap = bitmap;
    }
	public int getWidth()
    {
      return originalWidth;
    }
	public int getHeight()
    {
      return originalHeight;
    }
	public void setDimension(int width, int height)
    {
      this.originalWidth = width;
      this.originalHeight = height;
    }
}
