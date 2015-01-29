package lipuka.android.view;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.bornander.math.Vector2D;

public interface InfoGrafixObject {


	
	public abstract void draw(Canvas canvas, Matrix transform, Paint paint);

public abstract float getAngle();

public abstract void setAngle(float angle);

public abstract float getScale();

public abstract void setScale(float scale);

public abstract Vector2D getPosition();

public abstract void setPosition(Vector2D position);

public abstract int getCenterX();

public int getCenterY();
	
	public abstract boolean hitTest(float scrnX, float scrnY);

}
