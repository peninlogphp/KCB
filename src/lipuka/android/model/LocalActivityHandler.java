package lipuka.android.model;


import java.util.ArrayList;
import java.util.List;

import kcb.android.LipukaApplication;
import kcb.android.Main;

import kcb.android.R;
import lipuka.android.view.LipukaCheckBox;
import lipuka.android.view.LipukaEditText;
import lipuka.android.view.LipukaHiddenView;
import lipuka.android.view.LipukaImageView;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.LipukaNavigateButton;
import lipuka.android.view.LipukaRadioButton;
import lipuka.android.view.LipukaRadioGroup;
import lipuka.android.view.LipukaSubmitButton;
import lipuka.android.view.LipukaTextView;
import lipuka.android.view.LipukaWebView;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;






import android.net.Uri;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
	 
public class LocalActivityHandler extends DefaultHandler {
	     
	private StringBuffer buffer = new StringBuffer();
	     
    private LipukaTextView currentTextView;
    private LipukaRadioGroup currentRadioGroup;
    private List<LipukaListItem> lipukaListItems;
	private LinearLayout linearLayout;
	private Main activity;
	private String activityId;
	private boolean foundActivity = false;
	private byte activityType;
	LipukaListItem currentItem;
	private boolean foundWebView = false;
    private LipukaWebView currentWebView;
    private String currentDialogTitle;
    private String currentDialogMsg;
    LipukaApplication lipukaApplication;
    private LipukaList lipukaList;
    ScrollView rootLayout;

	public LocalActivityHandler(Main activity, String activityId){
		super();
 this.activity = activity;
 this.activityId = activityId;
 lipukaListItems = new ArrayList<LipukaListItem>();
 lipukaApplication = (LipukaApplication)activity.getApplication();
 rootLayout = (ScrollView)activity.getLayoutInflater().inflate(R.layout.main_layout, null);
 rootLayout.setBackgroundResource(R.drawable.layer_background);

linearLayout = (LinearLayout)rootLayout.findViewById(R.id.main_layout);
 
 //linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
 //linearLayout = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.main_layout, null);

 //linearLayout.setBackgroundResource(R.drawable.layer_background);

 lipukaApplication.setWebViewFlag(false);
 }
	 
	 @Override
	    public void startElement(String namespaceURI, String localName,
	            String qName, Attributes atts) throws SAXException {
	         	         
	        if (foundActivity) {
	        	if (localName.equals("submit")) {
	        		handleSubmit(atts);
	        	}
	        	else if (localName.equals("navigate")) {
	        		handleNavigate(atts);
		        }
	        	else if (localName.equals("list")) {
	        		handleList(atts);
		        }
		        else if (localName.equals("checkbox")) {
			        handleCheckbox(atts);
			        }
		        else if (localName.equals("edittext")) {
		        handleEditText(atts);
		        }
		        else if (localName.equals("imageview")) {
			        handleImageView(atts);
			        }
		        else if (localName.equals("radiogroup")) {
			        handleRadioGroup(atts);
			        }
		        else if (localName.equals("radiobutton")) {
			        handleRadioButton(atts);
			        }
		        else if (localName.equals("textview")) {
			        handleTextView(atts);
			        buffer.setLength(0);
			        }
		        else if (localName.equals("webview")) {
		        	foundWebView = true;
			        handleWebView(atts);
			        }
		        else if (localName.equals("hiddenview")) {
			        handleHiddenView(atts);
			        }
		        else if (localName.equals("media")) {
			        handleMedia(atts);
			        }
		        else if (localName.equals("item")) {
			        handleItem(atts);
			        buffer.setLength(0);
			        }
	        	}
	        else if (localName.equals("activity")) {
	        	if (atts.getValue("id").equals(activityId)) {
	        		foundActivity = true;

	        		String type = atts.getValue("type");
	        		if (type.equals("default")) {
	        			activityType = ServiceXMLParser.DEFAULT;
	        	
	        			 activity.setTitle(atts.getValue("title"));
			        }
	    	        else if (type.equals("list")) {
	        			activityType = ServiceXMLParser.LIST;
	        			lipukaListItems.clear();
	        	        activity.setActionBarContentView(R.layout.list_view);
	        	        activity.setTitle(atts.getValue("title"));	 

	    	        }
	    	        else if (type.equals("dialog")) {
	        			activityType = ServiceXMLParser.DIALOG;
	        			currentDialogTitle = atts.getValue("title");

	    	        }
		        }
	        }
	 
	    }
	     
	    @Override
	    public void endElement(String uri, String localName, String qName)throws SAXException {
	         
	     if(foundActivity){
	    	 if (localName.equals("textview")) {
	     
	        	 if (activityType == ServiceXMLParser.DIALOG){
		    		 currentDialogMsg = buffer.toString();
		    	 }else {
			        	currentTextView.setText(buffer.toString());		    		 
		    	 }
	        }
	        else if (localName.equals("item")) {
	        	currentItem.setText(buffer.toString());
	        }
	        else if (localName.equals("activity")) {
        		throw new SAXException("1");
	        }
	     }
	    }
	     
	    @Override
	    public void characters(char[] ch, int start, int length) {
		     if(foundActivity){
buffer.append(ch, start, length);
		     }
	    }
     
	     public void handleSubmit(Attributes atts){
	     	 LipukaSubmitButton view = (LipukaSubmitButton)
	   	  activity.getLayoutInflater().inflate(R.layout.lipuka_submit_button, null);
	    	 view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 view.setAction(atts.getValue("action"));
	    	 String attr = atts.getValue("method");
	    	 if(attr.equals("post")){
		    	 view.setMethod(LipukaSubmitButton.METHOD_POST);		 
	    	 }
	    	 else {
		    	 view.setMethod(LipukaSubmitButton.METHOD_GET);		 	    		 
	    	 }
	    	 attr = atts.getValue("bearer");
	    	 if(attr.equals("http")){
		    	 view.setBearer(LipukaSubmitButton.BEARER_HTTP);		 
	    	 }
	    	 else if(attr.equals("https")){
		    	 view.setBearer(LipukaSubmitButton.BEARER_HTTPS);		 	    		 
	    	 }
	    	 else {
		    	 view.setBearer(LipukaSubmitButton.BEARER_SMS);		 	    		 
	    	 }

	    	 view.setText(atts.getValue("label"));
	    	 attr = atts.getValue("pin");
	    	 if(attr.equals("true")){
		    	 view.setPIN(true);		 
	    	 }
	    	 else {
		    	 view.setPIN(false);		 	    		 
	    	 }
	    	 lipukaApplication.setLipukaSubmitButton(view);
		    	if (activityType == ServiceXMLParser.DEFAULT){
			    	 view.setOnClickListener(lipukaApplication.getLipukaActionListener());
			    	 linearLayout.addView(view);
		    	}else if (activityType == ServiceXMLParser.LIST){
		    		lipukaList.setNavigationType(LipukaList.NAVIGATION_REMOTE);
		    	}

	     }
	     public void handleNavigate(Attributes atts){
	    	 LipukaNavigateButton view = (LipukaNavigateButton)
		   	  activity.getLayoutInflater().inflate(R.layout.lipuka_navigate_button, null);
		 		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
 
	    	 view.setActivity(atts.getValue("activity"));
	    	 view.setText(atts.getValue("label"));
	       	 lipukaApplication.setLipukaNavigateButton(view);
	
	    	if (activityType == ServiceXMLParser.DEFAULT){
		    	 view.setOnClickListener(lipukaApplication.getLipukaActionListener());
		    	 linearLayout.addView(view);
	    	}else if (activityType == ServiceXMLParser.LIST){
	    		lipukaList.setNavigationType(LipukaList.NAVIGATION_LOCAL);
	    	}
	     }
	     public void handleCheckbox(Attributes atts){
	    	 LipukaCheckBox view = (LipukaCheckBox)activity.getLayoutInflater().inflate(R.layout.lipuka_check_box, null);

	    	 view.setID(atts.getValue("id"));
	    	 view.setText(atts.getValue("text"));
	    	 linearLayout.addView(view);
	     }
	     public void handleDatePicker(Attributes atts){

	     }
	     public void handleEditText(Attributes atts){
	    	 LinearLayout editTextLayout = new LinearLayout(activity);

	    	 editTextLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    	 editTextLayout.setOrientation(LinearLayout.HORIZONTAL);

	    	 LipukaEditText view = new LipukaEditText(activity);
	    	 String attr = null;
	    	 view.setID(atts.getValue("id"));
	    	 attr = atts.getValue("lines");
	    	 view.setMaxLines(Integer.valueOf(attr));
	    	 attr = atts.getValue("length");
	    	 InputFilter[] filterArray = new InputFilter[1];
	   filterArray[0] = new InputFilter.LengthFilter(Integer.valueOf(attr));
view.setFilters(filterArray);

attr = atts.getValue("required");
if(attr.equals("true")){
	 view.setRequired(true);		 
}
else {
	 view.setRequired(false);		 	    		 
}

int width = Integer.valueOf(atts.getValue("width"));
float scale = activity.getResources().getDisplayMetrics().density;
width =  (int) (width * scale + 0.5f);
view.setWidth(width);
view.setLayoutParams(new LayoutParams(width, LayoutParams.WRAP_CONTENT));
Log.d(Main.TAG, "width: "+width);

attr = atts.getValue("max");
if(attr.length() > 0){
	view.setMax(Float.valueOf(attr));
}

attr = atts.getValue("min");
if(attr.length() > 0){
	view.setMin(Float.valueOf(attr));
}

attr = atts.getValue("label");

if(attr.length() > 0){
	LipukaTextView tv = (LipukaTextView)activity.getLayoutInflater().inflate(R.layout.lipuka_text_view, null);
	tv.setText(attr);
	tv.setLayoutParams(new LayoutParams(width, LayoutParams.WRAP_CONTENT));
	editTextLayout.addView(tv);

}
attr = atts.getValue("type");
if(attr.equals("datetime")){
	 view.setInputType(InputType.TYPE_CLASS_DATETIME);		 
}
else if(attr.equals("number")){
	 view.setInputType(InputType.TYPE_CLASS_NUMBER);		 
}
else if(attr.equals("phone")){
	 view.setInputType(InputType.TYPE_CLASS_PHONE);		 
}
else if(attr.equals("date")){
	 //view.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
	 view.setInputType(InputType.TYPE_CLASS_NUMBER);		 

	 view.setOnTouchListener(activity.getLipukaDateFieldListener());
	// view.setDateSetListener(activity.getDateListener());
}
else if(attr.equals("time")){
	 view.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);		 
}
else if(attr.equals("decimal")){
	 view.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);		 
}
else if(attr.equals("email")){
	 view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);		 
}
else if(attr.equals("password")){
	 view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);		 
//editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
	 }
else if(attr.equals("url")){
	 view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);		 
}
else {
	 view.setInputType(InputType.TYPE_CLASS_TEXT);		 
}
	    	// view.setText(atts.getValue("text"));
editTextLayout.addView(view);
	    	 linearLayout.addView(editTextLayout);
	     }
	     public void handleImageView(Attributes atts){
LipukaImageView view = new LipukaImageView(activity);
view.setID(atts.getValue("id"));
view.setMaxWidth(Integer.valueOf(atts.getValue("width")));
view.setMaxHeight(Integer.valueOf(atts.getValue("height")));
view.setUrl(atts.getValue("src"));
linearLayout.addView(view);
	     }
	     public void handleRadioGroup(Attributes atts){
	    	 currentRadioGroup = new LipukaRadioGroup(activity);
	    	 currentRadioGroup.setID(atts.getValue("id"));
	    	 currentTextView = (LipukaTextView)activity.getLayoutInflater().inflate(R.layout.lipuka_text_view, null);
	    	 currentTextView.setText(atts.getValue("label"));
	    	 linearLayout.addView(currentTextView);	
	    	 linearLayout.addView(currentRadioGroup);
	     }
	     public void handleRadioButton(Attributes atts){
	    	 LipukaRadioButton view = (LipukaRadioButton)activity.getLayoutInflater().inflate(R.layout.lipuka_radio_button, null);
	    	 view.setText(atts.getValue("text"));
	    	 currentRadioGroup.addView(view);
	    	 
	    	 }
	     public void handleTextView(Attributes atts){
		    	 if (activityType == ServiceXMLParser.DEFAULT){
currentTextView = (LipukaTextView)activity.getLayoutInflater().inflate(R.layout.lipuka_text_view, null);
			    	 linearLayout.addView(currentTextView);		    		 
		    	 }

	     }
	     public void handleTimePicker(Attributes atts){

	     }
	     
	     public void handleWebView(Attributes atts){
	    	 currentWebView = (LipukaWebView)activity.getLayoutInflater().inflate(R.layout.lipuka_web_view, null);
	    	 currentWebView.setBackgroundColor(0);
	    	 currentWebView.setURL(atts.getValue("url"));
			    	 linearLayout.addView(currentWebView);
	     }
	     public void handleHiddenView(Attributes atts){
	    	 LipukaHiddenView view = new LipukaHiddenView(activity);
	    	 view.setName(atts.getValue("name"));
	    	 view.setValue(atts.getValue("value"));
			    	 linearLayout.addView(view);
	     }
	     public void handleMedia(Attributes atts){

	     }
	     public void handleItem(Attributes atts){
	    	 currentItem = new LipukaListItem();
	    	 currentItem.setValue(atts.getValue("value"));

	    	 lipukaListItems.add(currentItem);
	     }
	     public void handleList(Attributes atts){
	    	 lipukaList = new LipukaList();
	    	 lipukaList.setType(atts.getValue("type"));
	    	 lipukaList.setSource(atts.getValue("source"));
	    	 lipukaApplication.setLipukaList(lipukaList);
	     }  
	     public byte getActivityType(){
	    	 return activityType;
	     }
	     public List<LipukaListItem> getListItems(){
	    	 return lipukaListItems;
	     }
	     public LipukaWebView getWebView(){
	    	 return currentWebView;
	     }
	     public boolean isWebView(){
	    	 return foundWebView;
	     }
	     public ScrollView getRootLayout(){
	    	 return rootLayout;
	     }
	     public String getCurrentDialogTitle() { 
	    	 return this.currentDialogTitle;
	    	 	   }

	    	    public String getCurrentDialogMsg() { 
	        		if (currentDialogMsg == null || currentDialogMsg.length() == 0){
	        		 currentDialogMsg = "Sorry! An error occurred, please try again later";	
	        		}
	        		return this.currentDialogMsg;
	    	 	   	   }
	}