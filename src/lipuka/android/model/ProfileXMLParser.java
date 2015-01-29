package lipuka.android.model;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kcb.android.LipukaApplication;
import kcb.android.Main;

import lipuka.android.view.LipukaListItem;


import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;






import android.content.Intent;
import android.util.Log;
	 
public class ProfileXMLParser {

LipukaApplication lipukaApplication;
private boolean parsingSuccessful = false;

public ProfileXMLParser(LipukaApplication lipukaApplication){
	 super();
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
	     
	    public void parseMSISDN(String xml) {
    		parsingSuccessful = false;
      
	        try {	             
	            XMLReader xmlreader = initializeReader();
	           ProfileHandler profileHandler = 
	            	new ProfileHandler(lipukaApplication, ProfileHandler.MSISDN);
	            // assign our handler
	            xmlreader.setContentHandler(profileHandler);
	        	Log.d(Main.TAG, "Created Profile Handler");

	        try{    // perform the synchronous parse
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}else{
		        	Log.d(Main.TAG, "Parsing MSISDN error", se);
	
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
	    		Log.d(Main.TAG, "Parsing MSISDN Successful");
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
	    public boolean parsePinStatus(String xml, int currentClientID) {

    		parsingSuccessful = false;
      boolean pinStatus = false;
      ProfileHandler profileHandler = null;
	        try {	             
	            XMLReader xmlreader = initializeReader();
	            profileHandler = 
	            	new ProfileHandler(lipukaApplication, ProfileHandler.PIN_STATUS);
	            profileHandler.setCurrentClientID(currentClientID);
	            // assign our handler
	            xmlreader.setContentHandler(profileHandler);

	        try{    // perform the synchronous parse
	            xmlreader.parse(new InputSource(new StringReader(xml)));

	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){

	        		parsingSuccessful = true;
	        	}else{
		        	Log.d(Main.TAG, "Parsing MSISDN error", se);
	
	        	}
	        }
	        catch(Exception e){
	        	Log.d(Main.TAG, e.getMessage());
	        	Log.d(Main.TAG, "Pin status parsing error", e);

	        }
	       if (parsingSuccessful){
	            pinStatus = profileHandler.isPinStatus();
	    		Log.d(Main.TAG, "Parsing PinStatus Successful");
	        }

	             	             
	        }
	        catch (Exception e) {
	        	Log.d(Main.TAG, "Pin status parsing error", e);
	        }
	   return pinStatus;      
	    }
	    public ArrayList<BankAccount> parseAccounts(String xml) {
    		parsingSuccessful = false;
    		ProfileHandler profileHandler = 
            	new ProfileHandler(lipukaApplication, ProfileHandler.ACCOUNTS);
	        try {	             
	            XMLReader xmlreader = initializeReader();
	            
	            // assign our handler
	            xmlreader.setContentHandler(profileHandler);
	        	Log.d(Main.TAG, "Created Profile Handler");
if (xml == null){
	Log.d(Main.TAG, "xml is null");

}
	        try{    // perform the synchronous parse
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}
	        	else{
	        	Log.d(Main.TAG, "Error", se);
	        	}
	        }
	        catch(Exception e){
	        	Log.d(Main.TAG, "Error", e);

	        }
	       if (parsingSuccessful){
	    		Log.d(Main.TAG, "Parsing Accounts Successful");
	    	   return profileHandler.getBankAccounts();
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
	         return null;
	    }
	    
	    public List<String> parseNominations(String xml, int currentClientID) {
    		parsingSuccessful = false;
    		ProfileHandler profileHandler = null;
	        try {
	             
	            XMLReader xmlreader = initializeReader();

		           profileHandler = 
		            	new ProfileHandler(lipukaApplication, ProfileHandler.NOMINATIONS);
		            profileHandler.setCurrentClientID(currentClientID);
// assign our handler
		            xmlreader.setContentHandler(profileHandler);
		        	Log.d(Main.TAG, "Created Profile Handler");

	        try{    // perform the synchronous parse
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
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
	    		Log.d(Main.TAG, "Parsing NOMINATIONS Successful");
	    		return profileHandler.getNominations();
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
        	return null;

	    }
	    
	    public List<LipukaListItem> parseEnrollments(String xml, int currentClientID) {
    		parsingSuccessful = false;
    		ProfileHandler profileHandler = null;
	        try {
	             
	            XMLReader xmlreader = initializeReader();

		           profileHandler = 
		            	new ProfileHandler(lipukaApplication, ProfileHandler.ENROLLMENTS);
		            profileHandler.setCurrentClientID(currentClientID);
// assign our handler
		            xmlreader.setContentHandler(profileHandler);
		        	Log.d(Main.TAG, "Created Profile Handler");

	        try{    // perform the synchronous parse
	            xmlreader.parse(new InputSource(new StringReader(xml)));
	        }catch(SAXException se){
	        	if (se.getMessage().equals("1")){
	        		parsingSuccessful = true;
	        	}else{
	        	Log.d(Main.TAG, "Error", se);
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
	    		Log.d(Main.TAG, "Parsing ENROLLMENTS Successful");
	    		return profileHandler.getEnrollments();
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
        	return null;

	    }
	    
	}