package lipuka.android.model;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kcb.android.LipukaApplication;
import kcb.android.Main;

import kcb.android.R;
import lipuka.android.view.IconicAdapter;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.LipukaWebView;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;






import android.util.Log;
import android.widget.ListView;
	 
public class ServiceXMLParser {

public final static byte DEFAULT = 0;
public final static byte LIST = 1;
public final static byte DIALOG = 2;

private Main activity;
LipukaApplication lipukaApplication;
private boolean parsingSuccessful = false;

public ServiceXMLParser(Main activity,
		LipukaApplication lipukaApplication){
	 super();
	 this.activity = activity;
	 this.lipukaApplication = lipukaApplication;
}
	    private XMLReader initializeReader() throws ParserConfigurationException, SAXException {
	        SAXParserFactory factory = SAXParserFactory.newInstance();
	        // create a parser
	        SAXParser parser = factory.newSAXParser();
	        // create the reader (scanner)
	        XMLReader xmlreader = parser.getXMLReader();

	        return xmlreader;
	    }
	     
	    public void parseLocalActivity(String xml, String activityId) {
    		parsingSuccessful = false;
      
	        try {
	             
	            XMLReader xmlreader = initializeReader();

	            LocalActivityHandler activityHandler = 
	            	new LocalActivityHandler(activity, activityId);


	            // assign our handler
	            xmlreader.setContentHandler(activityHandler);
	        	Log.d(Main.TAG, "Created Handler");

	        try{    // perform the synchronous parse
	        	if(xml == null || xml.length() == 0)
	        	{	        	Log.d(Main.TAG, "xml is null");
}
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}else{
	        	Log.d(Main.TAG, "Parsing Error", se);
	        	}
	        }
	        catch(Exception e){
	        	Log.d(Main.TAG, "Error: ", e);

	        	StackTraceElement ste[] = e.getStackTrace();
	        	for(int i = 0; i < ste.length; i++){
	        	Log.d(Main.TAG, ste[i].getClassName());
		        	Log.d(Main.TAG, "\n"+ste[i].getLineNumber());	
	        	}
	        }
	       if (parsingSuccessful){
	    		Log.d(Main.TAG, "Parsing Successful");

	    	   if (activityHandler.getActivityType() == DEFAULT){
	            	activity.setActionBarContentView(activityHandler.getRootLayout());
	//activityHandler.getLinearLayout().startAnimation(activity.getAnimation());
	            	lipukaApplication.setCurrentActivityType(ServiceXMLParser.DEFAULT);
   if (activityHandler.isWebView()){
			            	LipukaWebView lipukaWebView =
			            		activityHandler.getWebView();
			            	lipukaWebView.load();
			            	lipukaApplication.setIsWebView(true);
			            	lipukaApplication.setWebView(lipukaWebView);

	            }
	            }
	            else if (activityHandler.getActivityType() == LIST){
	            	handleList(activityHandler.getListItems());
	            }
	            else if (activityHandler.getActivityType() == DIALOG){
lipukaApplication.setCurrentActivityType(ServiceXMLParser.DIALOG);
	 lipukaApplication.setCurrentDialogTitle(activityHandler.
			 getCurrentDialogTitle());
	 lipukaApplication.setCurrentDialogMsg(activityHandler.
			 getCurrentDialogMsg());
	 activity.showDialog(Main.DIALOG_MSG_ID);
	            }
	        }

	             	             
	        }
	        catch (Exception e) {
	        	Log.d(Main.TAG, e.getMessage());
	        	StackTraceElement ste[] = e.getStackTrace();
	        	for(int i = 0; i < ste.length; i++){
		        	Log.d(Main.TAG, ste[i].getClassName());
		        	Log.d(Main.TAG, "\n"+ste[i].getLineNumber());
	
	        	}
	        }
	    } 
	    
	    public void handleList(List<LipukaListItem> parsedLipukaListItems) {
	    	lipukaApplication.setCurrentActivityType(ServiceXMLParser.LIST);

        	ListView listView = (ListView) 
        	activity.findViewById(R.id.lipuka_list_view);
            listView.setOnItemClickListener(lipukaApplication.getLipukaActionListener());
            listView.setTextFilterEnabled(true);
 String listItems[]  = null;
            LipukaList lipukaList = lipukaApplication.getLipukaList();
List<LipukaListItem> lipukaListItems = new ArrayList<LipukaListItem>();

int size = 0;
if (lipukaList.getSource().length() == 0){
lipukaListItems = parsedLipukaListItems;

}else if (lipukaList.getSource().equals("nominations")){
List<String> nominations = lipukaApplication.parseNominations();
LipukaListItem lipukaListItem = null;
if(nominations != null && nominations.size() > 0){
	for (String nomination: nominations){
		lipukaListItem = new LipukaListItem();
		lipukaListItem.setText(nomination);
		lipukaListItem.setValue(nomination);
		lipukaListItems.add(lipukaListItem);
	}

}else{
	lipukaListItem = new LipukaListItem();
	lipukaListItem.setText("No nominations found");
	lipukaListItem.setValue("none");
	lipukaListItems.add(lipukaListItem);
}		
if(parsedLipukaListItems.size() > 0){
lipukaListItem = parsedLipukaListItems.get(0);
lipukaListItems.add(lipukaListItem);
}
lipukaApplication.setListItems(lipukaListItems);
}
else if (lipukaList.getSource().equals("enrollments")){
	List<LipukaListItem> enrollments = lipukaApplication.parseEnrollments();
	LipukaListItem lipukaListItem = null;
if(enrollments != null && enrollments.size() > 0){
		for (LipukaListItem enrollment: enrollments){
			lipukaListItems.add(enrollment);
		}

	}else{
		lipukaListItem = new LipukaListItem();
		lipukaListItem.setText("No enrollments found");
		lipukaListItem.setValue("none");
		lipukaListItems.add(lipukaListItem);	
	}
if(parsedLipukaListItems.size() > 0){
lipukaListItem = parsedLipukaListItems.get(0);
lipukaListItems.add(lipukaListItem);
}
lipukaApplication.setListItems(lipukaListItems);
}
else if (lipukaList.getSource().equals("accounts")){
	List<LipukaListItem> accounts = lipukaApplication.getCbsAccounts();
	LipukaListItem lipukaListItem = null;
if(accounts != null && accounts.size() > 0){
		for (LipukaListItem account: accounts){
			lipukaListItems.add(account);
		}

	}else{
		lipukaListItem = new LipukaListItem();
		lipukaListItem.setText("No relevant accounts found");
		lipukaListItem.setValue("none");
		lipukaListItems.add(lipukaListItem);
	}
lipukaApplication.setListItems(lipukaListItems);
}
else if (lipukaList.getSource().equals("myaccounts")){
	List<LipukaListItem> accounts = lipukaApplication.getCustomerAccounts();
	LipukaListItem lipukaListItem = null;
if(accounts != null && accounts.size() > 0){
		for (LipukaListItem account: accounts){
			lipukaListItems.add(account);
		}

	}else{
		lipukaListItem = new LipukaListItem();
		lipukaListItem.setText("No other accounts found");
		lipukaListItem.setValue("none");
		lipukaListItems.add(lipukaListItem);
	}
lipukaApplication.setListItems(lipukaListItems);
}
else if (lipukaList.getSource().equals("otheraccounts")){
	List<LipukaListItem> accounts = lipukaApplication.getOtherAccounts();
	LipukaListItem lipukaListItem = null;
if(accounts != null && accounts.size() > 0){
		for (LipukaListItem account: accounts){
			lipukaListItems.add(account);
		}

	}else{
		lipukaListItem = new LipukaListItem();
		lipukaListItem.setText("No other accounts found");
		lipukaListItem.setValue("none");
		lipukaListItems.add(lipukaListItem);
	}
if(parsedLipukaListItems.size() > 0){
	lipukaListItem = parsedLipukaListItems.get(0);
	lipukaListItems.add(lipukaListItem);
	}
lipukaApplication.setListItems(lipukaListItems);
}
else if (lipukaList.getSource().equals("contacts")){
LipukaListItem lipukaListItem = parsedLipukaListItems.get(0);
lipukaListItem.setValue(lipukaApplication.getMSISDN());
lipukaListItems.add(lipukaListItem);
lipukaListItems.add(parsedLipukaListItems.get(1));

	List<LipukaListItem> contacts = lipukaApplication.getContacts();
if(contacts != null && contacts.size() > 0){
		for (LipukaListItem contact: contacts){
			lipukaListItems.add(contact);
		}

	}else{
		lipukaListItem = new LipukaListItem();
		lipukaListItem.setText("No contacts found");
		lipukaListItem.setValue("none");
		lipukaListItems.add(lipukaListItem);
	}
lipukaApplication.setListItems(lipukaListItems);
}
size = lipukaListItems.size();
listItems = new String[size];
for (int i = 0; i < size; i++){
listItems[i] = lipukaListItems.get(i).getText();
}
listView.setAdapter(new IconicAdapter(activity,
        			listItems));
        	lipukaApplication.setListItems(lipukaListItems);
    	//listView.startAnimation(activity.getAnimation());
        	
	    }
	    public void parseRemoteActivity(String xml, boolean mainVisible) {
    		parsingSuccessful = false;

	        try {
	             
	            XMLReader xmlreader = initializeReader();

	            RemoteActivityHandler activityHandler = 
	            	new RemoteActivityHandler(activity);


	            // assign our handler
	            xmlreader.setContentHandler(activityHandler);
	        	Log.d(Main.TAG, "Created Handler");

	        try{    // perform the synchronous parse
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}else{
	        	Log.d(Main.TAG, "Parsing Error", se);
	        	}
	        }
	        catch(Exception e){
	        	Log.d(Main.TAG, e.getMessage());

	        	StackTraceElement ste[] = e.getStackTrace();
	        	for(int i = 0; i < ste.length; i++){
		        	Log.d(Main.TAG, ste[i].getClassName());
		        	Log.d(Main.TAG, "\n"+ste[i].getLineNumber());	
	        	}
	        }
	       if (parsingSuccessful){
	    		Log.d(Main.TAG, "Parsing Successful");

	    	   if (activityHandler.getActivityType() == DEFAULT){
	            	activity.setActionBarContentView(activityHandler.getRootLayout());
	            	lipukaApplication.setCurrentActivityType(ServiceXMLParser.DEFAULT);
   if (activityHandler.isWebView()){
			            	LipukaWebView lipukaWebView =
			            		activityHandler.getWebView();
			            	lipukaWebView.load();
			            	lipukaApplication.setIsWebView(true);
			            	lipukaApplication.setWebView(lipukaWebView);

	            }
	            }
	            else if (activityHandler.getActivityType() == LIST){
handleList(activityHandler.getListItems());
	            }
	            else if (activityHandler.getActivityType() == DIALOG){
	            	if(mainVisible){
lipukaApplication.setCurrentActivityType(ServiceXMLParser.DIALOG);
	 lipukaApplication.setCurrentDialogTitle(activityHandler.
			 getCurrentDialogTitle());
	 lipukaApplication.setCurrentDialogMsg(activityHandler.
			 getCurrentDialogMsg());
	 lipukaApplication.showDialog(Main.DIALOG_MSG_ID);
	            	}else{
	         lipukaApplication.showResponseNotification(activityHandler.getCurrentDialogMsg());      		
	            	}
	            }
	        }

	             	             
	        }
	        catch (Exception e) {
	        	Log.d(Main.TAG, e.getMessage());
	        	StackTraceElement ste[] = e.getStackTrace();
	        	for(int i = 0; i < ste.length; i++){
		        	Log.d(Main.TAG, ste[i].getClassName());
		        	Log.d(Main.TAG, "\n"+ste[i].getLineNumber());
	
	        	}
	        }
	         
	    }
	    
	    public void parseServiceVersion(String xml) {
    		parsingSuccessful = false;
      
	        try {
	             
	            XMLReader xmlreader = initializeReader();

	            ServiceVersionHandler serviceVersionHandler = 
	            	new ServiceVersionHandler(lipukaApplication);
	            serviceVersionHandler.setState(ServiceVersionHandler.DEFAULT);

	            // assign our handler
	            xmlreader.setContentHandler(serviceVersionHandler);
	        	Log.d(Main.TAG, "Created ServiceVersionHandler");

	        try{    // perform the synchronous parse
	        	if(xml == null || xml.length() == 0)
	        	{	        	Log.d(Main.TAG, "xml is null");
}
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}else{
	        	Log.d(Main.TAG, "Parsing Error", se);
	        	}
	        }
	        catch(Exception e){
	        	Log.d(Main.TAG, "Error: ", e);
	        }
	       if (parsingSuccessful){
	    		Log.d(Main.TAG, "Parsing Successful");
	        }

	             	             
	        }
	        catch (Exception e) {
	        	Log.d(Main.TAG, "Error: ", e);
	        }
	    } 
	    
	    public String parseServiceVersionForUpdate(String xml) {
    		parsingSuccessful = false;
      String version = null;
	        try {
	             
	            XMLReader xmlreader = initializeReader();

	            ServiceVersionHandler serviceVersionHandler = 
	            	new ServiceVersionHandler(lipukaApplication);
	            serviceVersionHandler.setState(ServiceVersionHandler.UPDATE);


	            // assign our handler
	            xmlreader.setContentHandler(serviceVersionHandler);
	        	Log.d(Main.TAG, "Created ServiceVersionHandler");

	        try{    // perform the synchronous parse
	        	if(xml == null || xml.length() == 0)
	        	{	        	Log.d(Main.TAG, "xml is null");
}
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}else{
	        	Log.d(Main.TAG, "Parsing Error", se);
	        	}
	        }
	        catch(Exception e){
	        	Log.d(Main.TAG, "Error: ", e);
	        }
	       if (parsingSuccessful){
	    	   version = serviceVersionHandler.getServiceVersionForUpdate();
	    		Log.d(Main.TAG, "Parsing Successful");
	        }

	             	             
	        }
	        catch (Exception e) {
	        	Log.d(Main.TAG, "Error: ", e);
	        }
	        return version;
	    } 
	}