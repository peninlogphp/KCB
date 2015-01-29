package lipuka.android.view.anim;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public final class SwapViews implements Runnable {
private boolean mIsFirstView;
View view1;
View view2;
View parentView;
public SwapViews(boolean isFirstView, View view1, View view2, View parentView) {
 mIsFirstView = isFirstView;
 this.view1 = view1;
 this.view2 = view2;
 this.parentView = parentView;
}

public void run() {
 final float centerX = view1.getWidth() / 2.0f;
 final float centerY = view1.getHeight() / 2.0f;
 Flip3dAnimation rotation;

 if (mIsFirstView) {
  view1.setVisibility(View.GONE);
  view2.setVisibility(View.VISIBLE);
  view2.requestFocus();
  if(parentView !=  null){
	 FrameLayout frameLayout = (FrameLayout) parentView;
	 frameLayout.bringChildToFront(view2);
	  }
     rotation = new Flip3dAnimation(-90, 0, centerX, centerY);
 } else {
  view2.setVisibility(View.GONE);
  view1.setVisibility(View.VISIBLE);
  view1.requestFocus();
  if(parentView !=  null){
		 FrameLayout frameLayout = (FrameLayout) parentView;
		 frameLayout.bringChildToFront(view1);
		  }
     rotation = new Flip3dAnimation(90, 0, centerX, centerY);
 }

 rotation.setDuration(500);
 rotation.setFillAfter(true);
 rotation.setInterpolator(new DecelerateInterpolator());

 if (mIsFirstView) {
  view2.startAnimation(rotation);
 } else {
  view1.startAnimation(rotation);
 }
 
}
}
