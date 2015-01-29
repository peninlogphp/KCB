package lipuka.android.view;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kcb.android.CFChome;
import kcb.android.Main;




import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;


import com.bornander.math.Vector2D;


public class CircleLayout extends View implements OnTouchListener {

	public static final  byte TRANSFORMATION_ROTATE = 1;
	public static final  byte TRANSFORMATION_SCALE = 2;
	public static final  byte TRANSFORMATION_TRANSLATE = 3;

	private Matrix transform = new Matrix();


	private TouchManager touchManager;

	// Debug helpers to draw lines between the two touch points
	private Vector2D vca = null;
	private Vector2D vcb = null;
	private Vector2D vpa = null;
	private Vector2D vpb = null;
	CFChome infoGrafixHome;
	int centerX = 0;
	int centerY = 0;
	int actionTitleHeight = 0;
	int displayWidth = 0, displayHeight = 0; 
byte transformation = TRANSFORMATION_ROTATE;
private ArrayList<InfoGrafixObject> images = new ArrayList<InfoGrafixObject>();
InfoGrafixObject selectedImage = null;
InfoGrafixObject lastSelectedImage = null;

Paint touchPaint;
private Path paintPath;
boolean paintMode = false;
private float mX, mY;
private static final float TOUCH_TOLERANCE = 4;
String title;
	public CircleLayout(Context context, String title) {
		super(context);
		
		infoGrafixHome = (CFChome)context;
this.title = title;

Resources res = context.getResources();
DisplayMetrics metrics = res.getDisplayMetrics();

this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
		metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
		metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);

centerX =  displayWidth / 2;
centerY = displayHeight / 2;
setOnTouchListener(this);
		
		touchManager = new TouchManager(2, centerX, centerY);
		
		touchPaint = new Paint();
		touchPaint.setAntiAlias(true);
		//touchPaint.setDither(true);
		touchPaint.setColor(0xFF000000);
		touchPaint.setStyle(Paint.Style.STROKE);
		touchPaint.setStrokeJoin(Paint.Join.ROUND);
		touchPaint.setStrokeCap(Paint.Cap.ROUND);
		touchPaint.setStrokeWidth(12);
		
        paintPath = new Path();

	}


	private static float getDegreesFromRadians(float angle) {
		return (float)(angle * 180.0 / Math.PI);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		for (InfoGrafixObject im: images){
			im.draw(canvas, transform, paint);
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		vca = null;
		vcb = null;
		vpa = null;
		vpb = null;
		if(selectedImage == null){
			selectedImage = selectObject(event.getX(), event.getY());
			lastSelectedImage = selectedImage;
		}
		try {
		if(transformation == TRANSFORMATION_ROTATE){	
			if(selectedImage != null){
				touchManager.update(event, 0, centerY+actionTitleHeight, true);				
			}
		}else{
			touchManager.update(event, 0, 0, false);			
		}
			if (touchManager.getPressCount() == 1) {
				
				vca = touchManager.getPoint(0);
				vpa = touchManager.getPreviousPoint(0);
				/*if(touchManager.isTestHit()){
					selectedImage = selectObject(event.getX(), event.getY());					
				}*/

				if(selectedImage != null && transformation == TRANSFORMATION_TRANSLATE){
					Log.d(Main.TAG, "delta x: "+touchManager.moveDelta(0).getX()); 
					Log.d(Main.TAG, "delta y: "+touchManager.moveDelta(0).getY()); 
					selectedImage.getPosition().add(touchManager.moveDelta(0));
					invalidate();
					Log.d(Main.TAG, "select for move: ");

			}
				//Log.d(TibaHome.TAG, "press count is 1: "); 

			}
			else {
				if (touchManager.getPressCount() == 2) {
					vca = touchManager.getPoint(0);
					vpa = touchManager.getPreviousPoint(0);
					vcb = touchManager.getPoint(1);
					vpb = touchManager.getPreviousPoint(1);
					/*if(touchManager.isTestHit()){
						//Log.d(TibaHome.TAG, "is test hit: "); 

						selectedImage = selectObject(event.getX(), event.getY());	
					}*/
					Vector2D current = touchManager.getVector(0, 1);
					Vector2D previous = touchManager.getPreviousVector(0, 1);
					float currentDistance = current.getLength();
					float previousDistance = previous.getLength();

					if (previousDistance != 0) {
						if(transformation == TRANSFORMATION_SCALE){
							if(selectedImage != null){
								float scale = selectedImage.getScale();
								scale *= currentDistance / previousDistance;
								selectedImage.setScale(scale);	
					invalidate();
					Log.d(Main.TAG, "select for scale: ");

							}
						}
					}

					float signedAngle = Vector2D.getSignedAngleBetween(current, previous);
					
					if(transformation == TRANSFORMATION_ROTATE){
						if(selectedImage != null){
							float angle = selectedImage.getAngle();
							angle -= signedAngle;
							selectedImage.setAngle(angle);	
				invalidate();
				Log.d(Main.TAG, "select for rotate: ");
						}
					}
		
				}
			}
			
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (touchManager.getPressCount() == 0) {
					selectedImage = null;
				}
				//infoGrafixHome.filterByMood(actualAngle);
	        }

		}
		catch(Throwable t) {
			// So lazy...
		}
		return true;

	}

	public void setTransformation(byte transformation)
    {
       this.transformation =  transformation;
    }
	public void addImage(InfoGrafixObject image)
    {
      images.add(image);
      invalidate();
    }
	public InfoGrafixObject selectObject(float scrnX, float scrnY) {
		int n = images.size();
		for (int i = n - 1; i >= 0; i--) {
			InfoGrafixObject im = images.get(i);
			if (im.hitTest(scrnX, scrnY)){
				images.remove(im);
				images.add(im);
				return im;
				}
		}
		return null;
	}
	public InfoGrafixObject getLastSelectedImage()
    {
      return lastSelectedImage;
    }
	public void colorChanged(int color) {
        touchPaint.setColor(color);
    }
	
     private void touch_start(float x, float y) {
        // paintPath.reset();
    	 paintPath.moveTo(x, y);
         mX = x;
         mY = y;
     }
     private void touch_move(float x, float y) {
         float dx = Math.abs(x - mX);
         float dy = Math.abs(y - mY);
         if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        	 paintPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
             mX = x;
             mY = y;
         }
     }
     private void touch_up() {
    	 paintPath.lineTo(mX, mY);
         // commit the path to our offscreen
        //mCanvas.drawPath(mPath, mPaint);
         // kill this so we don't double draw
        // mPath.reset();
     }
     
     @Override
     public boolean onTouchEvent(MotionEvent event) {
    		if(paintMode){	
  float x = event.getX();
         float y = event.getY();
         
         switch (event.getAction()) {
             case MotionEvent.ACTION_DOWN:
                 touch_start(x, y);
                 invalidate();
                 break;
             case MotionEvent.ACTION_MOVE:
                 touch_move(x, y);
                 invalidate();
                 break;
             case MotionEvent.ACTION_UP:
                 touch_up();
                 invalidate();
                 break;
         }
         return true;
    		}else{
    	         return false;    			
    		}
     }
     public void setPaintMode(boolean paintMode){
    	 this.paintMode = paintMode;
     }
}
