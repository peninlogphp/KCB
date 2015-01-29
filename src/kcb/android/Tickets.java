package kcb.android;


import java.util.ArrayList;
import java.util.List;



import org.json.JSONArray;
import org.json.JSONObject;

import pl.polidea.coverflow.CoverFlow;
import pl.polidea.coverflow.ReflectingImageAdapter;
import pl.polidea.coverflow.ResourceImageAdapter;

import com.devsmart.android.ui.HorizontalListView;






import kcb.android.R;
import lipuka.android.data.HomeItem;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
import lipuka.android.view.adapter.MerchantCategoriesAdapter;
import lipuka.android.view.adapter.TicketCategoriesAdapter;
import lipuka.android.view.anim.ExpandAnimation;
import lipuka.android.view.anim.LipukaAnim;
import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Tickets extends Activity implements OnClickListener, ResponseActivity, 
AdapterView.OnItemClickListener{
	   
	private TextView textView;
    private int[] resourceList = { R.drawable.fast_n_furious, R.drawable.european_film_festival, R.drawable.iron_man, R.drawable.soko_soko_craft_market,
        R.drawable.soul_concert};
    private String[] eventNames = {"Fast & Furious", "European Film Festival", "Iron Man 3", "Soko Soko Craft Market",
    		"Soul 2013 Concert"};
   
	RelativeLayout help;
	ImageButton closeHelp;

	LipukaApplication lipukaApplication;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.tickets);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Tickets");
	        
	        textView = (TextView) findViewById(R.id.statusText);
	        // note resources below are taken using getIdentifier to allow importing
	        // this library as library.
	        
	        final CoverFlow reflectingCoverFlow = (CoverFlow) findViewById(R.id.coverflowReflect);
	        setupCoverFlow(reflectingCoverFlow, true);
	        
	        GridView gridview = (GridView) findViewById(R.id.gridview);
	        List<HomeItem> homeItemList = new ArrayList<HomeItem>();
	        homeItemList.add(new HomeItem(0, "", "Movies", 0, true));
	        homeItemList.add(new HomeItem(0, "", "Sports", 0, true));
	        homeItemList.add(new HomeItem(0, "", "Concerts", 0, true));
	        homeItemList.add(new HomeItem(0, "", "Comedy", 0, true));

	        gridview.setAdapter(new TicketCategoriesAdapter(this, homeItemList));
	        gridview.setOnItemClickListener(this);
	        
     
	   	Button helpButton = (Button)findViewById(R.id.help);
		    helpButton.setOnClickListener(this);
		    Button homeButton = (Button)findViewById(R.id.home_button);
		    homeButton.setOnClickListener(this);

		    Button signOutButton = (Button)findViewById(R.id.sign_out);
		    signOutButton.setOnClickListener(this);
		   
			 help = (RelativeLayout) findViewById(R.id.help_layout);
		        WebView myWebView = (WebView) findViewById(R.id.webview);
		        WebSettings webSettings = myWebView.getSettings();
		        webSettings.setJavaScriptEnabled(true);
		    	myWebView.loadUrl("file:///android_asset/paybill.html");
		    	myWebView.setBackgroundColor(0);

		        closeHelp = (ImageButton) findViewById(R.id.close);
		        closeHelp.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                help.startAnimation(LipukaAnim.outToRightAnimation());
		            	help.setVisibility(View.GONE);
		            }
		        });  
	    }catch(Exception ex){
	    	Log.d(Main.TAG, "creating funds transfer error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(Tickets.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(Tickets.class, false);
	    }
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	       // MenuInflater inflater = getMenuInflater();
	      //  inflater.inflate(R.menu.help_menu, menu);
	        return true;
	    }
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle item selection
	        switch (item.getItemId()) {
	        case R.id.help:
	        	help.setVisibility(View.VISIBLE);
	        	help.startAnimation(LipukaAnim.inFromRightAnimation());   
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    } 
	/*    @Override
	    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

	        switch (item.getItemId()) {
	            case R.id.action_bar_view_help:
	            	help.setVisibility(View.VISIBLE);
	                help.startAnimation(LipukaAnim.inFromRightAnimation());
	                break;

	            default:
	                return super.onHandleActionBarItemClick(item, position);
	        }

	        return true;
	    }*/

	    		public void onClick(View arg0) {
	    		 if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			       // help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				 Intent i = new Intent(this, StanChartHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
	    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
	    	    	}else if (arg0.getId() == R.id.show_or_hide_pay_bills_saved){
	    	    		LinearLayout  savedBills = (LinearLayout) findViewById(R.id.pay_bills_saved);
	    	    		Drawable img = null;
		if(savedBills.isShown()){
			//savedBills.setVisibility(View.GONE);
			ExpandAnimation.collapse(savedBills);
  			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//savedBills.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(savedBills);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  merchants = (LinearLayout) findViewById(R.id.pay_bills_merchants);
		    	    		merchants.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideMerchants = (Button) findViewById(R.id.show_or_hide_pay_bills_merchants);
	    	    	        showOrHideMerchants.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		LinearLayout  other = (LinearLayout) findViewById(R.id.pay_bills_other);
		    	    		other.setVisibility(View.GONE);
	    	    	        Button showOrHideOther = (Button) findViewById(R.id.show_or_hide_pay_bills_other);
	    	    	        showOrHideOther.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_pay_bills_merchants){
		    	    		LinearLayout  merchants = (LinearLayout) findViewById(R.id.pay_bills_merchants);
		    	    		Drawable img = null;
			if(merchants.isShown()){
				//merchants.setVisibility(View.GONE);
				ExpandAnimation.collapse(merchants);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//merchants.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(merchants);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  savedBills = (LinearLayout) findViewById(R.id.pay_bills_saved);
			    	    		savedBills.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideSavedBills = (Button) findViewById(R.id.show_or_hide_pay_bills_saved);
		    	    	        showOrHideSavedBills.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    	        LinearLayout  other = (LinearLayout) findViewById(R.id.pay_bills_other);
			    	    		other.setVisibility(View.GONE);
		    	    	        Button showOrHideOther = (Button) findViewById(R.id.show_or_hide_pay_bills_other);
		    	    	        showOrHideOther.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_pay_bills_other){
			    	    		LinearLayout  other = (LinearLayout) findViewById(R.id.pay_bills_other);
			    	    		Drawable img = null;
				if(other.isShown()){
					//other.setVisibility(View.GONE);
					ExpandAnimation.collapse(other);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//other.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(other);
			    	    			img = getResources().getDrawable( R.drawable.hide );
			    	    			LinearLayout  savedBills = (LinearLayout) findViewById(R.id.pay_bills_saved);
				    	    		savedBills.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideSavedBills = (Button) findViewById(R.id.show_or_hide_pay_bills_saved);
			    	    	        showOrHideSavedBills.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	        LinearLayout  merchants = (LinearLayout) findViewById(R.id.pay_bills_merchants);
				    	    		merchants.setVisibility(View.GONE);
			    	    	        Button showOrHideMerchants = (Button) findViewById(R.id.show_or_hide_pay_bills_merchants);
			    	    	        showOrHideMerchants.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	 		}
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

				    	    	}else if (R.id.sign_out == arg0.getId()){
				    	    		lipukaApplication.setProfileID(0);
				    	    		Intent i = new Intent(this, StanChartHome.class);
				    	    		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    	    		startActivity(i);
					    	    	}
	    			}
	    		
	    		protected Dialog onCreateDialog(int id) {
	    	        Dialog dialog = null;
	    	        switch(id) {
	    	        case Main.DIALOG_MSG_ID:
	    	        	CustomDialog cd = new CustomDialog(this);
	    	        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = cd;
	    	        	break;
	    	        case Main.DIALOG_ERROR_ID:
	    	        	cd = new CustomDialog(this);
	    	        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = cd;
	    	        	break;
	    	        case Main.DIALOG_PROGRESS_ID:
	    	        	//builder = new AlertDialog.Builder(this);

	    	        	CustomProgressDialog pd = new CustomProgressDialog(this);
	    	        	dialog = pd;

	    	        	break;
	    	        case Main.DIALOG_PIN_ID:
	    	        	PinInputDialog pid = new PinInputDialog(this);
	    	        	pid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	pid.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = pid;
	    	        	break;
	    	        case Main.DIALOG_SERVICE_RESPONSE_ID:
	    	        	ResponseDialog rd = new ResponseDialog(this);
	    	        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = rd;
	    	        	break;
	    	        case Main.DIALOG_CONFIRM_ID:
	    	        	ConfirmationDialog cfd = new ConfirmationDialog(this);
	    	        	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	dialog = cfd;
	    	        	break;
	    	        default:
	    	            dialog = null;
	    	        }
	    	        return dialog;
	    	    }
	    	    protected  void onPrepareDialog(int id, Dialog dialog){
	    	    	//AlertDialog ad = (AlertDialog) dialog;
	    	    	switch(id) {
	    	        case Main.DIALOG_MSG_ID:
	    	        	CustomDialog cd = (CustomDialog)dialog;
	    	        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        case Main.DIALOG_ERROR_ID:
	    	          	cd = (CustomDialog)dialog;
	    	        	cd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        case Main.DIALOG_PROGRESS_ID:
	    	        	CustomProgressDialog pd = (CustomProgressDialog)dialog;
	    	ProgressBar pb = (ProgressBar)pd.findViewById(R.id.progressbar_default);
	    	pb.setVisibility(View.GONE);
	    	pb.setVisibility(View.VISIBLE);
	    	        	break;
	    	        case Main.DIALOG_PIN_ID:
	    	        	PinInputDialog pid = (PinInputDialog)dialog;
	    	        	pid.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	pid.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        case Main.DIALOG_SERVICE_RESPONSE_ID:
	    	        	ResponseDialog rd = (ResponseDialog)dialog;
	    	        	rd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	rd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        case Main.DIALOG_CONFIRM_ID:
	    	        	ConfirmationDialog cfd = (ConfirmationDialog)dialog;
	    	        	cfd.setCustomTitle(lipukaApplication.getCurrentDialogTitle());
	    	        	cfd.setMessage(lipukaApplication.getCurrentDialogMsg());
	    	        	break;
	    	        default:
	    	            dialog = null;
	    	        }
	    	    }
	    	    @Override
	    	    public void onConfigurationChanged(Configuration newConfig) {
	    	        super.onConfigurationChanged(newConfig);
	    	        }
	    		@Override
	    	    public void onUserInteraction()
	    	    {
	    	        super.onUserInteraction();
	    	        lipukaApplication.touch();
	    	    }
	    		
	   public class ConfirmationDialog extends Dialog implements OnClickListener {
	    			Button yesButton;
	    			Button noButton;
	    			TextView title;
	    			TextView message;
	    			public ConfirmationDialog(Context context) {
	    			super(context);

	    			/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
	    			requestWindowFeature(Window.FEATURE_NO_TITLE);
	    			/** Design the dialog in main.xml file */
	    			setContentView(R.layout.confirmation_dialog);
	    			yesButton = (Button) findViewById(R.id.yes_button);
	    			yesButton.setOnClickListener(this);
	    			noButton = (Button) findViewById(R.id.no_button);
	    			noButton.setOnClickListener(this);
	    			title = (TextView) findViewById(R.id.title);
	    			message = (TextView) findViewById(R.id.message);

	    			yesButton.setText("OK");
	    			noButton.setText("Cancel");
	    			}

	    			@Override
	    			public void onClick(View v) {
	    			/** When OK Button is clicked, dismiss the dialog */
	    			if (v == yesButton){
	    			dismiss();
	    			lipukaApplication.setCurrentDialogTitle("PIN");
  			      lipukaApplication.setCurrentDialogMsg("Please enter your mobile banking PIN");
  			showDialog(Main.DIALOG_PIN_ID);
	    		}else if (v == noButton){
	    			dismiss();
	    			}
	    			}

	    			public void setCustomTitle(String title) {
	    			this.title.setText(title);
	    			}
	    			public void setMessage(String message) {
	    				this.message.setText(message);
	    				}
	    			}
	   public void showResponse(){
			lipukaApplication.setCurrentDialogTitle("Response");
			 lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	           showDialog(Main.DIALOG_MSG_ID);
	   }
	   
	   /**
	     * Setup cover flow.
	     * 
	     * @param mCoverFlow
	     *            the m cover flow
	     * @param reflect
	     *            the reflect
	     */
	    private void setupCoverFlow(final CoverFlow mCoverFlow, final boolean reflect) {
	    	ResourceImageAdapter coverImageAdapter = new ResourceImageAdapter(this);
	        coverImageAdapter.setResources(resourceList);
	        ReflectingImageAdapter reflectingImageAdapter = new ReflectingImageAdapter(coverImageAdapter);
	        
	        mCoverFlow.setAdapter(reflectingImageAdapter);
	        mCoverFlow.setSelection(2, true);
	        setupListeners(mCoverFlow);
	    }

	    /**
	     * Sets the up listeners.
	     * 
	     * @param mCoverFlow
	     *            the new up listeners
	     */
	    private void setupListeners(final CoverFlow mCoverFlow) {
	        mCoverFlow.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(final AdapterView< ? > parent, final View view, final int position, final long id) {
	                //textView.setText("Item clicked! : " + id);
	    	    	Toast.makeText(Tickets.this, "Sorry, service is not yet available", Toast.LENGTH_LONG).show();
	            }

	        });
	        mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(final AdapterView< ? > parent, final View view, final int position, final long id) {
	                    	textView.setText(eventNames[position]);
	            }

	            @Override
	            public void onNothingSelected(final AdapterView< ? > parent) {
	               // textView.setText("Nothing clicked!");
	            }
	        });
	    }
	
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    	Toast.makeText(this, "Sorry, service is not yet available", Toast.LENGTH_LONG).show();
	    switch(position){
	    case 0:
	    	//Intent i = new Intent(MusicHome.this, BrowseCollections.class);
	       // startActivity(i);
	    		break;
	    case 1:
	    	/*Intent i = new Intent(MusicHome.this, MobileMoneyTransfer.class);
	        startActivity(i);*/
	    	break;
	    	
	    case 2:
	    	//i = new Intent(MusicHome.this, MoodTuner.class);
	       // startActivity(i);
	    		break;
	    	case 3:

	    		break;
	    	case 4:
	    		
	    		break;
	    	case 5:
	    		
	    		break;
	    	case 6:

	    		break;
	    	case 7:
	    		
	    		break;
	    	case 8:

	    		break;
	    	case 9:

	    		break;
	    	case 10:
	    		//i = new Intent(LipukaHome.this, Albums.class);
	    		//i = new Intent(MusicHome.this, AudioPlayLauncher.class);
	    	   //  startActivity(i);
	    		break;
	    	case 11:

	    		break;
	    	case 12:
	    	//	i = new Intent(MusicHome.this, FindAtms.class);
	    	  //  startActivity(i);
	    		break;
	    	case 13:

	    		break;
	    	case 14:
	    	//	 i = new Intent(MusicHome.this, ConfigureBankAccount.class);
	    	  //   startActivity(i);
	    		//	lipukaApplication.setChooseBankAction(1);

	    		break;
	    		default:
	    			
	    	} 

	    	    }
}