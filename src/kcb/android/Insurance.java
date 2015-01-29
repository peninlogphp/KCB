package kcb.android;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import kcb.android.R;
import lipuka.android.model.ActivityDateListener;
import lipuka.android.model.Navigation;
import lipuka.android.view.CustomDialog;
import lipuka.android.view.CustomProgressDialog;
import lipuka.android.view.EcobankDatePickerDialog;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.PinInputDialog;
import lipuka.android.view.ResponseDialog;
import lipuka.android.view.adapter.ComboBoxAdapter;
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
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemSelectedListener;

public class Insurance extends Activity implements OnClickListener, ResponseActivity,
DateCaptureActivity{
	   
	Button submitBuyCoverMotor, submitBuyCoverMedical,
	submitBuyCoverLifeInsurance, submitBuyCoverEducationPlan, renewCover, 
	claimCompensation, moreMotor, moreMedical, moreLifeInsurance, moreEducationPlan,
	backMotor, backMedical, backLifeInsurance, backEducationPlan,
	motor, medical, life, education, dateOfOccurrenceBtn;
	EditText motorBeneficiaryMobileNo, motorBeneficiaryName,
	motorBeneficiaryIdNo, medicalBeneficiaryMobileNo, medicalBeneficiaryName,
	medicalBeneficiaryIdNo, lifeInsuranceBeneficiaryMobileNo, lifeInsuranceBeneficiaryName,
	lifeInsuranceBeneficiaryIdNo, educationPlanBeneficiaryMobileNo, educationPlanBeneficiaryName,
	educationPlanBeneficiaryIdNo, educationPlanPeriod, natureOfClaim,
	motorDeclaredValue, lifeInsuranceAmount, educationPlanAmount;
	CheckBox motorAllowDirectDebit, medicalAllowDirectDebit, lifeInsuranceAllowDirectDebit,
	educationPlanAllowDirectDebit, renewCoverAllowDirectDebit;
	EditText dateOfOccurrence;
	RelativeLayout help;
	ImageButton closeHelp;

	String selectedVehicleCategory, selectedMotorCoverOption, 
	selectedMedicalCoverOption, selectedLifeInsuranceCoverOption, selectedEducationPlanCoverOption,
	selectedMotorPeriod, selectedLifeInsurancePeriod, selectedEducationPlanPeriod,
	selectedRenewCoverProvider, selectedClaimCompensationProvider, selectedRenewCoverProduct,
	selectedClaimCompensationProduct, selectedRenewCoverCoverOption, selectedRenewCoverPeriod;


	LipukaApplication lipukaApplication;
	LipukaListItem[] vehicleCategoriesArray, motorCoverOptionsArray,
	medicalCoverOptionsArray, lifeInsuranceCoverOptionsArray, educationPlanCoverOptionsArray,
	motorPeriodsArray, lifeInsurancePeriodsArray,
	renewCoverProvidersArray, claimCompensationProvidersArray, renewCoverProductArray,
	claimCompensationProductsArray, renewCoverCoverOptionsArray, renewCoverPeriodsArray,
	endowmentPlanPeriodsArray, termLifePeriodsArray;
	String amountStr, destinationStr;
	int selectedIndex;


	ViewFlipper buyCoverFlipper, heritageFlipper, cfcLifeFlipper;
	GestureDetector gestureDetector;
	int selectedBuyCoverOption, selectedRenewCoverOption, selectedClaimCompensationOption;
	LinearLayout heritage, cfcLife;
	LinearLayout heritageProductsMenu, motorInsurance, medicalBlueProduct,
	cfcLifeProductsMenu, lifeInsurance, educationPlan;
	TextView heritageText, cfcLifeText;

	ActivityDateListener activityDateListener;
	EditText currentDateField;
	
	byte service;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        lipukaApplication = (LipukaApplication)getApplication();
try{
	        setContentView(R.layout.insurance);
	        TextView title = (TextView) findViewById(R.id.title);
	        title.setText("Insurance");
	        motorBeneficiaryMobileNo = (EditText) findViewById(R.id.motor_beneficiary_mobile_no_field);
	        motorBeneficiaryName = (EditText) findViewById(R.id.motor_beneficiary_name_field);
	        motorBeneficiaryIdNo = (EditText) findViewById(R.id.motor_beneficiary_id_no_field);
	        medicalBeneficiaryMobileNo = (EditText) findViewById(R.id.medical_beneficiary_mobile_no_field);
	        medicalBeneficiaryName = (EditText) findViewById(R.id.medical_beneficiary_name_field);
	        medicalBeneficiaryIdNo = (EditText) findViewById(R.id.medical_beneficiary_id_no_field);
	        lifeInsuranceBeneficiaryMobileNo = (EditText) findViewById(R.id.life_insurance_beneficiary_mobile_no_field);
	        lifeInsuranceBeneficiaryName = (EditText) findViewById(R.id.life_insurance_beneficiary_name_field);
	        lifeInsuranceBeneficiaryIdNo = (EditText) findViewById(R.id.life_insurance_beneficiary_id_no_field);
	        educationPlanBeneficiaryMobileNo = (EditText) findViewById(R.id.education_plan_beneficiary_mobile_no_field);
	        educationPlanBeneficiaryName = (EditText) findViewById(R.id.education_plan_beneficiary_name_field);
	        educationPlanBeneficiaryIdNo = (EditText) findViewById(R.id.education_plan_beneficiary_id_no_field);
	        educationPlanPeriod = (EditText) findViewById(R.id.education_plan_period_field);
	        natureOfClaim = (EditText) findViewById(R.id.nature_of_claim_field);

	        motorDeclaredValue = (EditText) findViewById(R.id.motor_declared_value_field);
	        lifeInsuranceAmount = (EditText) findViewById(R.id.life_insurance_amount_field);
	        educationPlanAmount = (EditText) findViewById(R.id.education_plan_amount_field);

	        motorAllowDirectDebit = (CheckBox) findViewById(R.id.motor_allow_direct_debit);
	        medicalAllowDirectDebit = (CheckBox) findViewById(R.id.medical_allow_direct_debit);
	        lifeInsuranceAllowDirectDebit = (CheckBox) findViewById(R.id.life_insurance_allow_direct_debit);
	        educationPlanAllowDirectDebit = (CheckBox) findViewById(R.id.education_plan_allow_direct_debit);
	        renewCoverAllowDirectDebit = (CheckBox) findViewById(R.id.renew_cover_allow_direct_debit);
	        
	        dateOfOccurrence = (EditText) findViewById(R.id.date_of_occurrence_field);
	        dateOfOccurrence.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
	        dateOfOccurrence.setOnTouchListener(new EcobankDateFieldListener());
    
	        activityDateListener = new ActivityDateListener();

	        motorBeneficiaryMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);		
	        medicalBeneficiaryMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);		
	        lifeInsuranceBeneficiaryMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);		
	        educationPlanBeneficiaryMobileNo.setInputType(InputType.TYPE_CLASS_PHONE);		

	        motorBeneficiaryIdNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        medicalBeneficiaryIdNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        lifeInsuranceBeneficiaryIdNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        educationPlanBeneficiaryIdNo.setInputType(InputType.TYPE_CLASS_NUMBER);		
	        educationPlanPeriod.setInputType(InputType.TYPE_CLASS_NUMBER);		
	   
	        motorDeclaredValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        lifeInsuranceAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		
	        educationPlanAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		

	       final Spinner spinner = (Spinner) findViewById(R.id.vehicle_category_spinner);
	        spinner.setOnItemSelectedListener(new OnVehicleCategorySelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.vehicle_categories));
	         vehicleCategoriesArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	vehicleCategoriesArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating spinner list error", ex);
	
	    	}
	   	 ComboBoxAdapter adapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, vehicleCategoriesArray);
	   	spinner.setAdapter(adapter);
	   	
	   	final Spinner motorCoverOptionSpinner = (Spinner) findViewById(R.id.motor_cover_options_spinner);
	       motorCoverOptionSpinner.setOnItemSelectedListener(new OnMotorCoverOptionSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.motor_cover_options));
	         motorCoverOptionsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	motorCoverOptionsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter motorCoverOptionsAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, motorCoverOptionsArray);
	   	motorCoverOptionSpinner.setAdapter(motorCoverOptionsAdapter);
	   	
	    final Spinner motorPeriodSpinner = (Spinner) findViewById(R.id.motor_period_spinner);
	    motorPeriodSpinner.setOnItemSelectedListener(new OnMotorPeriodSelectedListener());
	
		  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.motor_periods));
	         motorPeriodsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	motorPeriodsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter motorPeriodsAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, motorPeriodsArray);
	   	motorPeriodSpinner.setAdapter(motorPeriodsAdapter);
	   	
	   	final Spinner medicalCoverOptionSpinner = (Spinner) findViewById(R.id.medical_cover_options_spinner);
	    medicalCoverOptionSpinner.setOnItemSelectedListener(new OnMedicalCoverOptionSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.medical_cover_options));
	  medicalCoverOptionsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	medicalCoverOptionsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating beneficiaries list error", ex);
	
	    	}
	   	 ComboBoxAdapter medicalCoverOptionAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, medicalCoverOptionsArray);
	   	medicalCoverOptionSpinner.setAdapter(medicalCoverOptionAdapter);
	   	
	   
	   	final Spinner lifeInsuranceCoverOptionSpinner = (Spinner) findViewById(R.id.life_insurance_cover_options_spinner);
	   	lifeInsuranceCoverOptionSpinner.setOnItemSelectedListener(new OnLifeInsuranceCoverOptionSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.life_insurance_cover_options));
	  lifeInsuranceCoverOptionsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	lifeInsuranceCoverOptionsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter lifeInsuranceCoverOptionAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, lifeInsuranceCoverOptionsArray);
	   	lifeInsuranceCoverOptionSpinner.setAdapter(lifeInsuranceCoverOptionAdapter);
	   	
	   	final Spinner lifeInsurancePeriodSpinner = (Spinner) findViewById(R.id.life_insurance_period_spinner);
	   	lifeInsurancePeriodSpinner.setOnItemSelectedListener(new OnLifeInsurancePeriodSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.endowment_plan_periods));
	  endowmentPlanPeriodsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	endowmentPlanPeriodsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	  	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.term_life_periods));
		  termLifePeriodsArray = new LipukaListItem[sources.length()];
		      JSONObject currentSource;
		        for(int i = 0; i < sources.length(); i++){
		        	currentSource = sources.getJSONObject(i);
		        	LipukaListItem lipukaListItem = new LipukaListItem("", 
		      currentSource.getString("name"), currentSource.getString("value"));
		        	termLifePeriodsArray[i]= lipukaListItem;   	
		        }
		        }catch(Exception ex){
			    	Log.d(Main.TAG, "creating list error", ex);
		
		    	}
	   	
		        ComboBoxAdapter lifeInsurancePeriodAdapter = new ComboBoxAdapter(Insurance.this, android.R.layout.simple_spinner_item, endowmentPlanPeriodsArray);
		 		lifeInsurancePeriodSpinner.setAdapter(lifeInsurancePeriodAdapter);
		 		
		 		final Spinner educationPlanCoverOptionSpinner = (Spinner) findViewById(R.id.education_plan_cover_options_spinner);
	   	educationPlanCoverOptionSpinner.setOnItemSelectedListener(new OnEducationPlanCoverOptionSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.education_plan_cover_options));
	  educationPlanCoverOptionsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	educationPlanCoverOptionsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter educationPlanCoverOptionAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, educationPlanCoverOptionsArray);
	   	educationPlanCoverOptionSpinner.setAdapter(educationPlanCoverOptionAdapter);
	   	
 		final Spinner renewCoverProviderSpinner = (Spinner) findViewById(R.id.renew_cover_provider_spinner);
 		renewCoverProviderSpinner.setOnItemSelectedListener(new OnRenewCoverProviderSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.renew_cover_providers));
	  renewCoverProvidersArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	renewCoverProvidersArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter renewCoverProviderAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, renewCoverProvidersArray);
	   	renewCoverProviderSpinner.setAdapter(renewCoverProviderAdapter);
	   
		final Spinner renewCoverProductSpinner = (Spinner) findViewById(R.id.renew_cover_product_spinner);
		renewCoverProductSpinner.setOnItemSelectedListener(new OnRenewCoverProductSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.renew_cover_products));
	  renewCoverProductArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	renewCoverProductArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter renewCoverProductAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, renewCoverProductArray);
	   	renewCoverProductSpinner.setAdapter(renewCoverProductAdapter);
	   	
	   	final Spinner renewCoverCoverOptionSpinner = (Spinner) findViewById(R.id.renew_cover_cover_options_spinner);
	   	renewCoverCoverOptionSpinner.setOnItemSelectedListener(new OnRenewCoverCoverOptionSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.motor_cover_options));
	  renewCoverCoverOptionsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	renewCoverCoverOptionsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter renewCoverCoverOptionAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, renewCoverCoverOptionsArray);
	   	renewCoverCoverOptionSpinner.setAdapter(renewCoverCoverOptionAdapter);
	   	
	 	final Spinner renewCoverPeriodSpinner = (Spinner) findViewById(R.id.renew_cover_period_spinner);
	 	renewCoverPeriodSpinner.setOnItemSelectedListener(new OnRenewCoverPeriodSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.motor_periods));
	  renewCoverPeriodsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	renewCoverPeriodsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter renewCoverPeriodAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, renewCoverPeriodsArray);
	   	renewCoverPeriodSpinner.setAdapter(renewCoverPeriodAdapter);
	   	
 		final Spinner claimCompensationProviderSpinner = (Spinner) findViewById(R.id.claim_compensation_provider_spinner);
 		claimCompensationProviderSpinner.setOnItemSelectedListener(new OnClaimCompensationProviderSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.claim_compensation_providers));
	  claimCompensationProvidersArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	claimCompensationProvidersArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter claimCompensationProviderAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, claimCompensationProvidersArray);
	   	claimCompensationProviderSpinner.setAdapter(claimCompensationProviderAdapter);
	   
		final Spinner claimCompensationProductSpinner = (Spinner) findViewById(R.id.claim_compensation_product_spinner);
		claimCompensationProductSpinner.setOnItemSelectedListener(new OnClaimCompensationProductSelectedListener());
	        
	  try{      JSONArray sources = new JSONArray(lipukaApplication.loadSpinnerData(R.raw.claim_compensation_products));
	  claimCompensationProductsArray = new LipukaListItem[sources.length()];
	      JSONObject currentSource;
	        for(int i = 0; i < sources.length(); i++){
	        	currentSource = sources.getJSONObject(i);
	        	LipukaListItem lipukaListItem = new LipukaListItem("", 
	      currentSource.getString("name"), currentSource.getString("value"));
	        	claimCompensationProductsArray[i]= lipukaListItem;   	
	        }
	        }catch(Exception ex){
		    	Log.d(Main.TAG, "creating list error", ex);
	
	    	}
	   	 ComboBoxAdapter claimCompensationProductAdapter = new ComboBoxAdapter(this, android.R.layout.simple_spinner_item, claimCompensationProductsArray);
	   	claimCompensationProductSpinner.setAdapter(claimCompensationProductAdapter);
	   	
        submitBuyCoverMotor = (Button) findViewById(R.id.buy_cover_heritage_motor_submit);
        submitBuyCoverMotor.setOnClickListener(this);
        submitBuyCoverMedical = (Button) findViewById(R.id.buy_cover_heritage_medical_submit);
        submitBuyCoverMedical.setOnClickListener(this);        
        submitBuyCoverLifeInsurance = (Button) findViewById(R.id.buy_cover_cfc_life_life_insurance_submit);
	    submitBuyCoverLifeInsurance.setOnClickListener(this);
	    submitBuyCoverEducationPlan = (Button) findViewById(R.id.buy_cover_cfc_life_education_plan_submit);
	    submitBuyCoverEducationPlan.setOnClickListener(this);
	    renewCover = (Button) findViewById(R.id.renew_cover_submit);
	    renewCover.setOnClickListener(this);
	    claimCompensation = (Button) findViewById(R.id.claim_compensation_submit);
	    claimCompensation.setOnClickListener(this);
	    moreMotor = (Button) findViewById(R.id.motor_more);
	    moreMotor.setOnClickListener(this);
	    moreMedical = (Button) findViewById(R.id.medical_more);
	    moreMedical.setOnClickListener(this);
	    moreLifeInsurance = (Button) findViewById(R.id.life_insurance_more);
	    moreLifeInsurance.setOnClickListener(this);
	    moreEducationPlan = (Button) findViewById(R.id.education_plan_more);
	    moreEducationPlan.setOnClickListener(this);
	    backMotor = (Button) findViewById(R.id.motor_back_icon);
	    backMotor.setOnClickListener(this);
	    backMedical = (Button) findViewById(R.id.medical_back_icon);
	    backMedical.setOnClickListener(this);
	    backLifeInsurance = (Button) findViewById(R.id.life_insurance_back_icon);
	    backLifeInsurance.setOnClickListener(this);
	    backEducationPlan = (Button) findViewById(R.id.education_plan_back_icon);
	    backEducationPlan.setOnClickListener(this);
	    motor = (Button) findViewById(R.id.motor_insurance);
	    motor.setOnClickListener(this);
	    medical = (Button) findViewById(R.id.medical_blue_product);
	    medical.setOnClickListener(this);
	    life = (Button) findViewById(R.id.life_insurance);
	    life.setOnClickListener(this);
	    education = (Button) findViewById(R.id.education_plan);
	    education.setOnClickListener(this);
	    dateOfOccurrenceBtn = (Button) findViewById(R.id.date_of_occurrence_button);
	    dateOfOccurrenceBtn.setOnClickListener(this); 
	    	
	        Button showOrHideBuyCover = (Button) findViewById(R.id.show_or_hide_buy_cover);
	        showOrHideBuyCover.setOnClickListener(this);
	        Button showOrHideRenewCover = (Button) findViewById(R.id.show_or_hide_renew_cover);
	        showOrHideRenewCover.setOnClickListener(this);
	        
	        Button showOrHideClaimCompensation = (Button) findViewById(R.id.show_or_hide_claim_compensation);
	        showOrHideClaimCompensation.setOnClickListener(this);
	        
	       
	   	
	        gestureDetector = new GestureDetector(this, new MyGestureDetector());
	        buyCoverFlipper = (ViewFlipper) findViewById(R.id.buy_cover_flipper);
	        heritageFlipper = (ViewFlipper) findViewById(R.id.heritage_flipper);
	        cfcLifeFlipper = (ViewFlipper) findViewById(R.id.cfc_life_flipper);
	       
	        
	        heritage = (LinearLayout)findViewById(R.id.insurance_buy_cover_heritage);
	        cfcLife = (LinearLayout)findViewById(R.id.insurance_buy_cover_cfc_life);
	        heritageProductsMenu = (LinearLayout)findViewById(R.id.insurance_buy_cover_heritage_products_menu);
	        motorInsurance = (LinearLayout)findViewById(R.id.insurance_buy_cover_heritage_motor);
	        medicalBlueProduct = (LinearLayout)findViewById(R.id.insurance_buy_cover_heritage_medical);
	        cfcLifeProductsMenu = (LinearLayout)findViewById(R.id.insurance_buy_cover_cfc_life_products_menu);
	        lifeInsurance = (LinearLayout)findViewById(R.id.insurance_buy_cover_cfc_life_life_insurance);
	        educationPlan = (LinearLayout)findViewById(R.id.insurance_buy_cover_cfc_life_education_plan);


	    	
	        heritageText = (TextView)findViewById(R.id.heritage_text);
	        cfcLifeText = (TextView)findViewById(R.id.cfc_life_text);
	        heritageText.setOnClickListener(this);
	        cfcLifeText.setOnClickListener(this);
	       
	 
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
	    	Log.d(Main.TAG, "creating insurance error", ex);

    	}
			lipukaApplication.setCurrentActivity(this);
		 	
	    }
	  
	    @Override
	    protected void onStart() {
	        super.onStart();
			lipukaApplication.setCurrentActivity(this);
			lipukaApplication.setActivityState(Insurance.class, true);
			}
	   
	    @Override
	    protected void onStop() {
	        super.onStop();
			lipukaApplication.setActivityState(Insurance.class, false);
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
	    			 if (submitBuyCoverMotor == arg0){
	    		    lipukaApplication.clearNavigationStack();
	
		boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String declaredValueStr = motorDeclaredValue.getText().toString();	
String mobileNoStr = motorBeneficiaryMobileNo.getText().toString();	
	    			String nameStr = motorBeneficiaryName.getText().toString();	
	    			String idNoStr = motorBeneficiaryIdNo.getText().toString();	
	    			
	    			if(declaredValueStr == null || declaredValueStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Declared value is missing\n");
	    			}
	    			if(mobileNoStr == null || mobileNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Mobile number is missing\n");
	    			}
	    			if(nameStr == null || nameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Name is missing\n");
	    			}
	    			
	    			if(idNoStr == null || idNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("ID number is missing\n");
	    			}
	    			if(!motorAllowDirectDebit.isChecked()){
	    				valid = false;
	    				errorBuffer.append("Please make sure you allow direct debit\n");
	    			}    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(idNoStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to buy a motor insurance cover, "+
	    			    			    		 selectedMotorCoverOption+ " option, with a premium of Ksh. 15,000/-, for a period of "+selectedMotorPeriod+
	    			    			    		". Press \"OK\" to buy now or \"Cancel\" to edit buy cover details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
	    			    				showResponse();

	    			    			destinationStr =  "Dear Alice, you have successfully bought a motor insurance cover, "+
	    			    			    		 selectedMotorCoverOption+" option, with a premium of Ksh. 15,000/-, for a period of "+selectedMotorPeriod+". Thank you";   

service = 1;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	

	    		}else if (submitBuyCoverMedical == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();	
	    			String mobileNoStr = medicalBeneficiaryMobileNo.getText().toString();	
	    			String nameStr = medicalBeneficiaryName.getText().toString();	
	    			String idNoStr = medicalBeneficiaryIdNo.getText().toString();	
	    			
	    			if(mobileNoStr == null || mobileNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Mobile number is missing\n");
	    			}
	    			if(nameStr == null || nameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Name is missing\n");
	    			}
	    			
	    			if(idNoStr == null || idNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("ID number is missing\n");
	    			}
	    			if(!medicalAllowDirectDebit.isChecked()){
	    				valid = false;
	    				errorBuffer.append("Please make sure you allow direct debit\n");
	    			}    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(idNoStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to buy a medical insurance cover, "+
	    			    			    		 selectedMedicalCoverOption+
	    			    			    		" option, with a premium of Ksh. 15,000/-. Press \"OK\" to buy now or \"Cancel\" to edit buy cover details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
	    			    				showResponse();

	    			    			destinationStr =  "Dear Alice, you have successfully bought a medical insurance cover, "+
	    			    			selectedMedicalCoverOption+" option, with a premium of Ksh. 15,000/-. Thank you";   

service = 1;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}		
	    		}else if (submitBuyCoverLifeInsurance == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    			String amountStr = lifeInsuranceAmount.getText().toString();	
	    			String mobileNoStr = lifeInsuranceBeneficiaryMobileNo.getText().toString();	
	    			String nameStr = lifeInsuranceBeneficiaryName.getText().toString();	
	    			String idNoStr = lifeInsuranceBeneficiaryIdNo.getText().toString();	
	    			
	    			if(amountStr == null || amountStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Amount is missing\n");
	    			}
	    			if(mobileNoStr == null || mobileNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Mobile number is missing\n");
	    			}
	    			if(nameStr == null || nameStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("Name is missing\n");
	    			}
	    			
	    			if(idNoStr == null || idNoStr.length() == 0){
	    				valid = false;
	    				errorBuffer.append("ID number is missing\n");
	    			}
	    			if(!lifeInsuranceAllowDirectDebit.isChecked()){
	    				valid = false;
	    				errorBuffer.append("Please make sure you allow direct debit\n");
	    			}    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			payloadBuffer.append(idNoStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    			/*	lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    				if(selectedLifeInsuranceCoverOption.equals("Whole Life")){
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to buy a life insurance cover, "+
	    			    			    		 selectedLifeInsuranceCoverOption+ " option, worth Ksh. "+amountStr+", with a premium of Ksh. 15,000/-. Press \"OK\" to buy now or \"Cancel\" to edit buy cover details");
	    			    				}else{
	    			    				     lipukaApplication.setCurrentDialogMsg("You are about to buy a life insurance cover, "+
		    			    			    		 selectedLifeInsuranceCoverOption+ " option, worth Ksh. "+amountStr+", with a premium of Ksh. 15,000/-, for a period of "+selectedLifeInsurancePeriod+
		    			    			    		". Press \"OK\" to buy now or \"Cancel\" to edit buy cover details");    			   							
	    			    				}
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);		*/
	    			    				showResponse();

    			    				if(selectedLifeInsuranceCoverOption.equals("Whole Life")){
		destinationStr =  "Dear Alice, you have successfully bought a life insurance cover, "+
	    			    			    		 selectedLifeInsuranceCoverOption+" option, worth Ksh. "+amountStr+", with a premium of Ksh. 15,000/-. Thank you";   
    			    				}else{
    			    					destinationStr =  "Dear Alice, you have successfully bought a life insurance cover, "+
			    			    		 selectedLifeInsuranceCoverOption+" option, worth Ksh. "+amountStr+", with a premium of Ksh. 15,000/-, for a period of "+selectedLifeInsurancePeriod+". Thank you";   		    				    					
    			    				}
service = 1;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	
	    		}else if (submitBuyCoverEducationPlan == arg0){
	  		    lipukaApplication.clearNavigationStack();
	    			
	  			
    			boolean valid = true;
    			StringBuffer errorBuffer = new StringBuffer();
    			String amountStr = educationPlanAmount.getText().toString();	
String educationPlanPeriodStr = educationPlanPeriod.getText().toString();	
String mobileNoStr = educationPlanBeneficiaryMobileNo.getText().toString();	
    			String nameStr = educationPlanBeneficiaryName.getText().toString();	
    			String idNoStr = educationPlanBeneficiaryIdNo.getText().toString();	
    			
    			if(amountStr == null || amountStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Amount is missing\n");
    			}
    			if(educationPlanPeriodStr == null || educationPlanPeriodStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Period is missing\n");
    			}
    			if(educationPlanPeriodStr.length() > 0){
    	        	int period = Integer.parseInt(educationPlanPeriodStr);

    	        	if(selectedEducationPlanCoverOption.equals("Educator Plan")){
    	        		if(period < 10 || period > 20){
            				valid = false;
            				errorBuffer.append("Period should be between 10 and 20 years\n");
            			}
        			}else{
        				if(period < 15){
            				valid = false;
            				errorBuffer.append("Period should be 15 or more years\n");
            			}	
        			}
    			}
    			if(mobileNoStr == null || mobileNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Mobile number is missing\n");
    			}
    			if(nameStr == null || nameStr.length() == 0){
    				valid = false;
    				errorBuffer.append("Name is missing\n");
    			}
    			
    			if(idNoStr == null || idNoStr.length() == 0){
    				valid = false;
    				errorBuffer.append("ID number is missing\n");
    			}
    			if(!educationPlanAllowDirectDebit.isChecked()){
    				valid = false;
    				errorBuffer.append("Please make sure you allow direct debit\n");
    			}    			
    			    			if(valid){
    			    				StringBuffer payloadBuffer = new StringBuffer();
    			    			payloadBuffer.append(idNoStr+"|");	    				
    			    	   			Navigation nav = new Navigation();
    			    			    nav.setPayload(payloadBuffer.toString());
    			    				lipukaApplication.pushNavigationStack(nav);
    			    				lipukaApplication.setPin(null);
    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
    			    			      lipukaApplication.setCurrentDialogMsg("You are about to buy an education insurance cover, "+
    			    			    		 selectedEducationPlanCoverOption+ " option, worth Ksh. "+amountStr+", with a premium of Ksh. 15,000, for a period of "+educationPlanPeriodStr+
    			    			    		" years. Press \"OK\" to buy now or \"Cancel\" to edit buy cover details");
    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
    			    				showResponse();

    			    			destinationStr =  "Dear Alice, you have successfully bought an education insurance cover, "+
    			    			selectedEducationPlanCoverOption+" option, worth Ksh. "+amountStr+", with a premium of Ksh. 15,000, for a period of "+educationPlanPeriodStr+" years. Thank you";   

service = 1;
    			    			}else{
    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
    			    			}	
	    		}else if (renewCover == arg0){
		  		    lipukaApplication.clearNavigationStack();
	    			
		  			
	    			boolean valid = true;
	    			StringBuffer errorBuffer = new StringBuffer();
	    	
	    			if(!renewCoverAllowDirectDebit.isChecked()){
	    				valid = false;
	    				errorBuffer.append("Please make sure you allow direct debit\n");
	    			}    			
	    			    			if(valid){
	    			    				StringBuffer payloadBuffer = new StringBuffer();
	    			    			//payloadBuffer.append(idNoStr+"|");	    				
	    			    	   			Navigation nav = new Navigation();
	    			    			    nav.setPayload(payloadBuffer.toString());
	    			    				lipukaApplication.pushNavigationStack(nav);
	    			    				lipukaApplication.setPin(null);
	    			    				/*lipukaApplication.setCurrentDialogTitle("Confirm");
	    			    			      lipukaApplication.setCurrentDialogMsg("You are about to set automatic renewal of your motor insurance cover with "+
	    			    			    		 selectedRenewCoverCoverOption+ " option, a premium of Ksh. 15,000/-, for a period of "+selectedRenewCoverPeriod+
	    			    			    		". Press \"OK\" to confirm this or \"Cancel\" to edit renew cover details");
	    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/	
	    			    				showResponse();

	    			    			destinationStr =  "Dear Alice, you have successfully set automatic renewal of your motor insurance cover with "+
	    			    			selectedRenewCoverCoverOption+" option, a premium of Ksh. 15,000/-, for a period of "+selectedRenewCoverPeriod+". Thank you";   

	service = 1;
	    			    			}else{
	    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
	    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
	    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
	    			    			}	
		    		}else if (claimCompensation == arg0){
			  		    lipukaApplication.clearNavigationStack();
		    			
			  			
		    			boolean valid = true;
		    			StringBuffer errorBuffer = new StringBuffer();
		    			String natureOfClaimStr = natureOfClaim.getText().toString();	
 			String dateOfOccurrenceStr = dateOfOccurrence.getText().toString();	
		    			Calendar enteredDateOfOccurrence = null;
	
		    			if(natureOfClaimStr == null || natureOfClaimStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Nature of claim is missing\n");
		    			}
		    			if(dateOfOccurrenceStr == null || dateOfOccurrenceStr.length() == 0){
		    				valid = false;
		    				errorBuffer.append("Date of occurrence is missing\n");
		    			}
		    			try{	
		        			if(valid){
		    	
		        				enteredDateOfOccurrence = Calendar.getInstance();

		    	        	StringTokenizer tokens = new StringTokenizer(dateOfOccurrenceStr, "-");
		    	        	int yr = Integer.parseInt(tokens.nextToken());
		    	        	int mth = Integer.parseInt(tokens.nextToken())-1;
		    	        	int day = Integer.parseInt(tokens.nextToken());
		    	        	
		    	        	enteredDateOfOccurrence.set(yr, mth, day);
		    	        	
		    	        	Calendar currentDate = Calendar.getInstance();
		    	        	
		    	        	if(currentDate.before(enteredDateOfOccurrence)){
		    	        		valid = false;
		    					errorBuffer.append("Date of occurrence should not be in the future\n");	
		    	        	}
		        			}
		        		}catch (NumberFormatException nfe){
		        			valid = false;
		    				errorBuffer.append("Enter a valid date value\n");			
		        		}catch (Exception e){
		        			valid = false;
		    				errorBuffer.append("Enter a valid date value\n");		
		        		} 			
		    			    			if(valid){
		    			    				StringBuffer payloadBuffer = new StringBuffer();
		    			    		//	payloadBuffer.append(idNoStr+"|");	    				
		    			    	   			Navigation nav = new Navigation();
		    			    			    nav.setPayload(payloadBuffer.toString());
		    			    				lipukaApplication.pushNavigationStack(nav);
		    			    				lipukaApplication.setPin(null);
		    			    			/*	lipukaApplication.setCurrentDialogTitle("Confirm");
		    			    			      lipukaApplication.setCurrentDialogMsg("You are about to report a claim to "+
		    			    			    		 selectedClaimCompensationProvider+ " under your motor insurance cover, date of occurrence is "+dateOfOccurrenceStr+
		    			    			    		". Press \"OK\" to report claim now or \"Cancel\" to edit claim details");
		    			    			showDialog(Main.DIALOG_CONFIRM_ID);	*/
		    			    				showResponse();
		    			    			destinationStr =  "Dear Alice, you have successfully reported a claim to "+
		    			    			    		 selectedClaimCompensationProvider+ " under your motor insurance cover, date of occurrence is "+dateOfOccurrenceStr+". Thank you";   

		service = 1;
		    			    			}else{
		    			    				lipukaApplication.setCurrentDialogTitle("Validation Error");
		    			    	        	lipukaApplication.setCurrentDialogMsg(errorBuffer.toString());
		    			    	            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
		    			    			}	
			    		}else if(arg0.getId() == R.id.heritage_text){
	    			 if (selectedBuyCoverOption == 1) {
		     			    buyCoverFlipper.setInAnimation(inFromLeftAnimation());
		     			    buyCoverFlipper.setOutAnimation(outToRightAnimation());
		     			    buyCoverFlipper.setDisplayedChild(0);
		     			    selectedBuyCoverOption = 0;
		     		   setSelectedBuyCoverBg();
		     			   }else{
		     				   return;
		     			   }			   
		     	}else if(arg0.getId() == R.id.cfc_life_text){
		     		if (selectedBuyCoverOption == 0) {
		     		    buyCoverFlipper.setInAnimation(inFromRightAnimation());
		     		    buyCoverFlipper.setOutAnimation(outToLeftAnimation());
		     		    buyCoverFlipper.setDisplayedChild(1);
		     		    selectedBuyCoverOption = 1;
		     			   setSelectedBuyCoverBg();
		     		   }else{
		     			   return;
		     		   }			   
		     }else if(arg0.getId() == R.id.motor_insurance){
		    	 heritageFlipper.setInAnimation(inFromRightAnimation());
   			   heritageFlipper.setOutAnimation(outToLeftAnimation());
   			  heritageFlipper.setDisplayedChild(1);		   
	     	}else if(arg0.getId() == R.id.medical_blue_product){
		    	 heritageFlipper.setInAnimation(inFromRightAnimation());
	   			   heritageFlipper.setOutAnimation(outToLeftAnimation());
	   			  heritageFlipper.setDisplayedChild(2);		   
		     	}else if(arg0.getId() == R.id.motor_back_icon ||
		     			arg0.getId() == R.id.medical_back_icon){
			    	 heritageFlipper.setInAnimation(inFromLeftAnimation());
		   			   heritageFlipper.setOutAnimation(outToRightAnimation());
		   			  heritageFlipper.setDisplayedChild(0);		   
			   }else if(arg0.getId() == R.id.life_insurance){
				    	 cfcLifeFlipper.setInAnimation(inFromRightAnimation());
				    	 cfcLifeFlipper.setOutAnimation(outToLeftAnimation());
				    	 cfcLifeFlipper.setDisplayedChild(1);		   
				}else if(arg0.getId() == R.id.education_plan){
					cfcLifeFlipper.setInAnimation(inFromRightAnimation());
					cfcLifeFlipper.setOutAnimation(outToLeftAnimation());
					cfcLifeFlipper.setDisplayedChild(2);		   
				}else if(arg0.getId() == R.id.life_insurance_back_icon ||
					     			arg0.getId() == R.id.education_plan_back_icon){
					cfcLifeFlipper.setInAnimation(inFromLeftAnimation());
					cfcLifeFlipper.setOutAnimation(outToRightAnimation());
					cfcLifeFlipper.setDisplayedChild(0);		   					   			   
				}else if(arg0.getId() ==  R.id.help){
					//help.setVisibility(View.VISIBLE);
			       // help.startAnimation(LipukaAnim.inFromRightAnimation());
	    		}else if(arg0.getId() ==  R.id.home_button){
				 Intent i = new Intent(this, StanChartHome.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
	    		}else if (closeHelp == arg0){
	    			help.startAnimation(LipukaAnim.outToRightAnimation());
	    	    	help.setVisibility(View.GONE);
	    	    	}else if (arg0.getId() == R.id.show_or_hide_buy_cover){
	    	    		LinearLayout  buyCover = (LinearLayout) findViewById(R.id.insurance_buy_cover);
	    	    		Drawable img = null;
		if(buyCover.isShown()){
			//buyCover.setVisibility(View.GONE);
			ExpandAnimation.collapse(buyCover);
  			img = getResources().getDrawable( R.drawable.show );
	    	    		}else{
	    	    			//buyCover.setVisibility(View.VISIBLE);
	    	    			ExpandAnimation.expand(buyCover);
	    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    		LinearLayout  renewCover = (LinearLayout) findViewById(R.id.insurance_renew_cover);
		    	    		renewCover.setVisibility(View.GONE);
		    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
	    	    	        Button showOrHideRenewCover = (Button) findViewById(R.id.show_or_hide_renew_cover);
	    	    	        showOrHideRenewCover.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
	    	    	 		LinearLayout  claimCompensation = (LinearLayout) findViewById(R.id.insurance_claim_compensation);
		    	    		claimCompensation.setVisibility(View.GONE);
	    	    	        Button showOrHideClaimCompensation = (Button) findViewById(R.id.show_or_hide_claim_compensation);
	    	    	        showOrHideClaimCompensation.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	

	    	    		}
		((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

		    	    	}else if (arg0.getId() == R.id.show_or_hide_renew_cover){
		    	    		LinearLayout  renewCover = (LinearLayout) findViewById(R.id.insurance_renew_cover);
		    	    		Drawable img = null;
			if(renewCover.isShown()){
				//renewCover.setVisibility(View.GONE);
				ExpandAnimation.collapse(renewCover);
				img = getResources().getDrawable( R.drawable.show );
		    	    		}else{
		    	    			//renewCover.setVisibility(View.VISIBLE);
		    	    			ExpandAnimation.expand(renewCover);
		    	    			img = getResources().getDrawable( R.drawable.hide );
		    	    			LinearLayout  buyCover = (LinearLayout) findViewById(R.id.insurance_buy_cover);
			    	    		buyCover.setVisibility(View.GONE);
			    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
		    	    	        Button showOrHideBuyCover = (Button) findViewById(R.id.show_or_hide_buy_cover);
		    	    	        showOrHideBuyCover.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
		    	    	        LinearLayout  claimCompensation = (LinearLayout) findViewById(R.id.insurance_claim_compensation);
			    	    		claimCompensation.setVisibility(View.GONE);
		    	    	        Button showOrHideClaimCompensation = (Button) findViewById(R.id.show_or_hide_claim_compensation);
		    	    	        showOrHideClaimCompensation.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
}
			((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.show_or_hide_claim_compensation){
			    	    		LinearLayout  claimCompensation = (LinearLayout) findViewById(R.id.insurance_claim_compensation);
			    	    		Drawable img = null;
				if(claimCompensation.isShown()){
					//claimCompensation.setVisibility(View.GONE);
					ExpandAnimation.collapse(claimCompensation);
					img = getResources().getDrawable( R.drawable.show );
			    	    		}else{
			    	    			//claimCompensation.setVisibility(View.VISIBLE);
			    	    			ExpandAnimation.expand(claimCompensation);
			    	    			img = getResources().getDrawable( R.drawable.hide );
			    	    			LinearLayout  buyCover = (LinearLayout) findViewById(R.id.insurance_buy_cover);
				    	    		buyCover.setVisibility(View.GONE);
				    	    		Drawable img2 = getResources().getDrawable( R.drawable.show );
			    	    	        Button showOrHideBuyCover = (Button) findViewById(R.id.show_or_hide_buy_cover);
			    	    	        showOrHideBuyCover.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	        LinearLayout  renewCover = (LinearLayout) findViewById(R.id.insurance_renew_cover);
				    	    		renewCover.setVisibility(View.GONE);
			    	    	        Button showOrHideRenewCover = (Button) findViewById(R.id.show_or_hide_renew_cover);
			    	    	        showOrHideRenewCover.setCompoundDrawablesWithIntrinsicBounds( img2, null, null, null );	
			    	    	 		}
				((Button)arg0).setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );	

			    	    	}else if (arg0.getId() == R.id.date_of_occurrence_button){
			        			currentDateField = dateOfOccurrence;
			    	        	showDialog(Main.DATE_DIALOG_ID);
			        	    }else if (arg0.getId() == R.id.motor_more){
			        	    	Intent i = new Intent(Intent.ACTION_VIEW);
			        	    	i.setData(Uri.parse("http://www.heritageinsurance.co.ke/motor.php"));
			        	    	//startActivity(i);
			        	    }else if (arg0.getId() == R.id.medical_more){
			        	    	Intent i = new Intent(Intent.ACTION_VIEW);
			        	    	i.setData(Uri.parse("http://www.heritageinsurance.co.ke/medical.php"));
			        	    	//startActivity(i);
			        	    }else if (arg0.getId() == R.id.life_insurance_more){
			        	    	Intent i = new Intent(Intent.ACTION_VIEW);
			        	    	i.setData(Uri.parse("http://www.cfclife.co.ke/products/life-insurance"));
			        	    	//startActivity(i);
			        	    }else if (arg0.getId() == R.id.education_plan_more){
			        	    	Intent i = new Intent(Intent.ACTION_VIEW);
			        	    	i.setData(Uri.parse("http://www.cfclife.co.ke/products/education-plan"));
			        	    	//startActivity(i);
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
	    	        case Main.DATE_DIALOG_ID:
	    	        	dialog = new EcobankDatePickerDialog(this);

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
	    	        case Main.DATE_DIALOG_ID:

	    	        	EcobankDatePickerDialog dpd = (EcobankDatePickerDialog)dialog;
	    	        	dpd.resetToCurrentDate();
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
	    	
	    		public class OnVehicleCategorySelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedVehicleCategory = vehicleCategoriesArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnMotorCoverOptionSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMotorCoverOption = motorCoverOptionsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    	
	    		public class OnMotorPeriodSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMotorPeriod = motorPeriodsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnMedicalCoverOptionSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedMedicalCoverOption = medicalCoverOptionsArray[pos].getText();
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnLifeInsuranceCoverOptionSelectedListener implements OnItemSelectedListener {
boolean firstTimeSelection = true;
		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	if(firstTimeSelection){
		 	        		firstTimeSelection = false;
			 	        	selectedLifeInsuranceCoverOption = lifeInsuranceCoverOptionsArray[pos].getText();
		 	        		return;
		 	        	}
		 	        	selectedLifeInsuranceCoverOption = lifeInsuranceCoverOptionsArray[pos].getText();
			 	   	   	final LinearLayout lifeInsurancePeriodLayout = (LinearLayout) findViewById(R.id.life_insurance_period_layout);
 	   	final Spinner lifeInsurancePeriodSpinner = (Spinner) findViewById(R.id.life_insurance_period_spinner);
 	if(selectedLifeInsuranceCoverOption.equals("Endowment Plan")){
 		 ComboBoxAdapter lifeInsurancePeriodAdapter = new ComboBoxAdapter(Insurance.this, android.R.layout.simple_spinner_item, endowmentPlanPeriodsArray);
 		lifeInsurancePeriodSpinner.setAdapter(lifeInsurancePeriodAdapter);
 		lifeInsurancePeriodLayout.setVisibility(View.VISIBLE);    				 	        		
			 	        			 	        	}else if(selectedLifeInsuranceCoverOption.equals("Term Life")){
 	   			 	 ComboBoxAdapter lifeInsurancePeriodAdapter = new ComboBoxAdapter(Insurance.this, android.R.layout.simple_spinner_item, termLifePeriodsArray);
 	   			 	        			 		lifeInsurancePeriodSpinner.setAdapter(lifeInsurancePeriodAdapter);
 	   			 	        			 	lifeInsurancePeriodLayout.setVisibility(View.VISIBLE);    				 	        		
 	        			 		}else{
 	        			 			lifeInsurancePeriodLayout.setVisibility(View.GONE);    				 	        		
		 	        			 	        	}
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		
	    		public class OnLifeInsurancePeriodSelectedListener implements OnItemSelectedListener {
	    			boolean firstTimeSelection = true;
		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	if(firstTimeSelection){
		 	        		firstTimeSelection = false;
			 	        	selectedLifeInsurancePeriod = endowmentPlanPeriodsArray[pos].getText();
		 	        		return;
		 	        	}
	    			 	if(selectedLifeInsuranceCoverOption.equals("Endowment Plan")){
			 	        	selectedLifeInsurancePeriod = endowmentPlanPeriodsArray[pos].getText();
    				 	        		
	    						 	        			 	        	}else if(selectedLifeInsuranceCoverOption.equals("Term Life")){
	    					selectedLifeInsurancePeriod = termLifePeriodsArray[pos].getText();
    				 	        		
	    						 	        			 	        	}
	    					 	        	}
	    					 	        public void onNothingSelected(AdapterView parent) {
	    					 	          // Do nothing.
	    					 	        }
	    					 	    }
	    		
	    		public class OnEducationPlanCoverOptionSelectedListener implements OnItemSelectedListener {
	    			boolean firstTimeSelection = true;
		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	if(firstTimeSelection){
		 	        		firstTimeSelection = false;
			 	        	selectedEducationPlanCoverOption = educationPlanCoverOptionsArray[pos].getText();
			 	        	TextView educationPlanPeriodLabel = (TextView) findViewById(R.id.education_plan_period_label);
			 	       	educationPlanPeriodLabel.setText("Period (10-20 years)");    		
	return;
		 	        	}
		 	        	selectedEducationPlanCoverOption = educationPlanCoverOptionsArray[pos].getText();
		 	        	TextView educationPlanPeriodLabel = (TextView) findViewById(R.id.education_plan_period_label);
if(selectedEducationPlanCoverOption.equals("Educator Plan")){
	educationPlanPeriodLabel.setText("Period (10-20 years)");    		
		 	        	}else{
educationPlanPeriodLabel.setText("Period (15 or more years)");    				 	        		
		 	        	}
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		
	    		public class OnRenewCoverProviderSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedRenewCoverProvider = renewCoverProvidersArray[pos].getText();		 	      
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnRenewCoverProductSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedRenewCoverProduct = renewCoverProductArray[pos].getText();		 	      
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnRenewCoverCoverOptionSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedRenewCoverCoverOption = renewCoverCoverOptionsArray[pos].getText();		 	      
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnRenewCoverPeriodSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedRenewCoverPeriod = renewCoverPeriodsArray[pos].getText();		 	      
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnClaimCompensationProviderSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedClaimCompensationProvider = claimCompensationProvidersArray[pos].getText();		 	      
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
		 	    }
	    		public class OnClaimCompensationProductSelectedListener implements OnItemSelectedListener {

		 	        public void onItemSelected(AdapterView<?> parent,
		 	            View view, int pos, long id) {
		 	        	selectedClaimCompensationProduct = claimCompensationProductsArray[pos].getText();		 	      
		 	        	}
		 	        public void onNothingSelected(AdapterView parent) {
		 	          // Do nothing.
		 	        }
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
		     /* lipukaApplication.setCurrentDialogMsg(destinationStr);
		      lipukaApplication.setDialogType(Main.DIALOG_SERVICE_RESPONSE_ID);
		      showDialog(Main.DIALOG_SERVICE_RESPONSE_ID);*/
			lipukaApplication.setCurrentDialogMsg("Sorry, service is not yet available");
	           showDialog(Main.DIALOG_MSG_ID);
	   }
	   
		private void increaseBuyCoverSelected(){
    		selectedBuyCoverOption++;
    		if(selectedBuyCoverOption == 2){
    			selectedBuyCoverOption = 0;
    		}
    	}
    	private void dereaseBuyCoverSelected(){
    		selectedBuyCoverOption--;
    		if(selectedBuyCoverOption == -1){
    			selectedBuyCoverOption = 1;
    		}
    	}
		private void increaseReceiveMoneySelected(){
    		selectedRenewCoverOption++;
    		if(selectedRenewCoverOption == 2){
    			selectedRenewCoverOption = 0;
    		}
    	}
    	private void dereaseReceiveMoneySelected(){
    		selectedRenewCoverOption--;
    		if(selectedRenewCoverOption == -1){
    			selectedRenewCoverOption = 1;
    		}
    	}
    	private void setSelectedBuyCoverBg(){
    		Resources res = getResources();
    		 switch (selectedBuyCoverOption) {
    	     case 0:
    	  heritageText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	  cfcLifeText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	         break;
    	     case 1:
    	    	  heritageText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_unselected ));
    	    	  cfcLifeText.setBackgroundDrawable(res.getDrawable(R.drawable.tab_selected ));
    	    	          break;
    	         default:
    	             return;
    	     }
    	}

    	@Override
    	public boolean dispatchTouchEvent(MotionEvent ev) {
    	    if (gestureDetector != null) {
    	        gestureDetector.onTouchEvent(ev);
    	    }
    	    return super.dispatchTouchEvent(ev);
    	}
    	class MyGestureDetector implements GestureDetector.OnGestureListener {
			
			final float scale = getResources().getDisplayMetrics().density;
	private final int SWIPE_MIN_DISTANCE = (int) (120 * scale + 0.5f);
			  private final int SWIPE_MAX_OFF_PATH = (int) (250 * scale + 0.5f);
			  private final int SWIPE_THRESHOLD_VELOCITY = (int) (400 * scale + 0.5f);

			  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
					
			   if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
			    return false;
			   if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
			     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				   buyCoverFlipper.setInAnimation(inFromRightAnimation());
				    buyCoverFlipper.setOutAnimation(outToLeftAnimation());
				    buyCoverFlipper.showNext();
				    increaseBuyCoverSelected();
					   setSelectedBuyCoverBg();
			   } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
			     && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				   buyCoverFlipper.setInAnimation(inFromLeftAnimation());
				    buyCoverFlipper.setOutAnimation(outToRightAnimation());
				    buyCoverFlipper.showPrevious();
				    dereaseBuyCoverSelected();
							   setSelectedBuyCoverBg();
			   			   }
			   return true;
			  }

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			}
		
		protected Animation inFromRightAnimation() {
	   	 
	        Animation inFromRight = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, +1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        inFromRight.setDuration(500);
	        inFromRight.setInterpolator(new AccelerateInterpolator());
	        return inFromRight;
	}

	protected Animation outToLeftAnimation() {
	        Animation outtoLeft = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, -1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoLeft.setDuration(500);
	        outtoLeft.setInterpolator(new AccelerateInterpolator());
	        return outtoLeft;
	}

	protected Animation inFromLeftAnimation() {
	        Animation inFromLeft = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, -1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        inFromLeft.setDuration(500);
	        inFromLeft.setInterpolator(new AccelerateInterpolator());
	        return inFromLeft;
	}

	protected Animation outToRightAnimation() {
	        Animation outtoRight = new TranslateAnimation(
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, +1.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f,
	                        Animation.RELATIVE_TO_PARENT, 0.0f);
	        outtoRight.setDuration(500);
	        outtoRight.setInterpolator(new AccelerateInterpolator());
	        return outtoRight;
	}
	    public class EcobankDateFieldListener implements View.OnTouchListener{

            public EcobankDateFieldListener(){

            }

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//Log.d(SalamaSureMain.TAG, "View ID: "+((LipukaEditText)v).getID()); 
				EditText editText = (EditText)v;
				currentDateField = editText;
				activityDateListener.setEditText(editText);
	        	showDialog(Main.DATE_DIALOG_ID);	
			
			return true;
		}
    }
	    public void setDate(String date){
			currentDateField.setText(date);		
		}
	    	    }