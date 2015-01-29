package lipuka.android.model;


import java.util.ArrayList;
import java.util.List;

import kcb.android.LipukaApplication;
import kcb.android.Main;

import lipuka.android.view.LipukaListItem;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;






import android.util.Log;
	 
public class ProfileHandler extends DefaultHandler {
	     
	public static final byte MSISDN = 1;
	public static final byte NOMINATIONS = 2;
	public static final byte ENROLLMENTS = 3;
	public static final byte ACCOUNTS = 4;
	public static final byte PIN_STATUS = 5;

	private StringBuffer buffer = new StringBuffer();
	     
	private List<String> nominations;
	private List<LipukaListItem> enrollments;
	private boolean foundMSISDN = false, foundPinStatus = false, pinStatus = false;
	private byte profileDataType;
    LipukaApplication lipukaApplication;
    ArrayList<Bank> banks;
    ArrayList<BankAccount> bankAccounts;

    Bank currentBank;
    int currentClientID, clientID;
    public ProfileHandler(LipukaApplication lipukaApplication, byte profileDataType){
		super();
 this.lipukaApplication = lipukaApplication;
 this.profileDataType = profileDataType;

 }
 
	 @Override
	    public void startElement(String namespaceURI, String localName,
	            String qName, Attributes atts) throws SAXException {
     	//Log.d(Main.TAG, "found: "+localName);
        
	        	if (localName.equals("msisdn") && profileDataType == ProfileHandler.MSISDN) {
	        		handleMSISDM(atts);
	        	}
	        	else if (localName.equals("banks") && profileDataType == ProfileHandler.ACCOUNTS) {
	handleBanks(atts);
		        }
	        	else if (localName.equals("bank") && profileDataType == ProfileHandler.ACCOUNTS) {
	        		handleBank(atts);
		        }
	         	else if (localName.equals("account") && profileDataType == ProfileHandler.ACCOUNTS) {
	        		handleAccount(atts);
		        }
	        	else if (localName.equals("nominations") && profileDataType == ProfileHandler.NOMINATIONS) {
	        		handleNominations(atts);
		        }
		        else if (localName.equals("nomination") && profileDataType == ProfileHandler.NOMINATIONS) {
			        handleNomination(atts);
			        }
		        else if (localName.equals("enrollments") && profileDataType == ProfileHandler.ENROLLMENTS) {
		        handleEnrollments(atts);
		        }
		        else if (localName.equals("enrollment") && profileDataType == ProfileHandler.ENROLLMENTS) {
			        handleEnrollment(atts);
			        }
		        else if (localName.equals("pin_status") && profileDataType == ProfileHandler.PIN_STATUS) {
		        	handlePinStatus(atts);
		        		        }
	        	else if (localName.equals("bank") && (profileDataType == ProfileHandler.PIN_STATUS ||
	   profileDataType == ProfileHandler.NOMINATIONS || profileDataType == ProfileHandler.ENROLLMENTS)) {
	        		handleBankClientID(atts);
	        			        }

	    }
	     
	    @Override
	    public void endElement(String uri, String localName, String qName)throws SAXException {
	         
	    	 if (localName.equals("msisdn") && profileDataType == ProfileHandler.MSISDN) {
	        		lipukaApplication.setMSISDN(buffer.toString());
	        		throw new SAXException("1");
	        }
	    	 else if (localName.equals("bank") && profileDataType == ProfileHandler.ACCOUNTS) {
	    		 if (bankAccounts != null){
		    		 throw new SAXException("1");
		    		    	 }
		        }
	    	 else if (localName.equals("nominations") && profileDataType == ProfileHandler.NOMINATIONS && currentClientID == clientID) {
        		throw new SAXException("1");
	        }
	        else if (localName.equals("enrollments") && profileDataType == ProfileHandler.ENROLLMENTS && currentClientID == clientID) {
        		throw new SAXException("1");
	        }
	        else if (localName.equals("pin_status") && profileDataType == ProfileHandler.PIN_STATUS && foundPinStatus) {
        		throw new SAXException("1");
	        }
	    }
	     
	    @Override
	    public void characters(char[] ch, int start, int length) {
		     if(foundMSISDN){
buffer.append(ch, start, length);
		     }else if(foundPinStatus){
		    		//Log.d(Main.TAG, "pinStatus: "+ch[0]);

		    	 if(ch[0] == '1'){
pinStatus = true;
	//Log.d(Main.TAG, "pinStatus set to true: ");

		    	 }
		     }
	    }
	     
	     public void handleMSISDM(Attributes atts){
	    	 foundMSISDN = true;
	    	 buffer.setLength(0);

	     }
	     public void handlePinStatus(Attributes atts){
	    	 if(currentClientID == clientID){
		    	 foundPinStatus = true;    		 
	    	 }

	     }
	     public void handleBanks(Attributes atts){
	    	 banks = new ArrayList<Bank>();

	     }
	     public void handleBank(Attributes atts){
	    	 if (Integer.valueOf(atts.getValue("clientID")) == LipukaApplication.CLIENT_ID){
		    	 bankAccounts = new ArrayList<BankAccount>();
	     }
	     }
	     public void handleAccount(Attributes atts){

	    	 if (bankAccounts != null){
	    		 BankAccount account = new BankAccount(Integer.valueOf(atts.getValue("accountID")),
	    		    	 atts.getValue("alias"), atts.getValue("type"));
	    		 bankAccounts.add(account);
	    		    	 }
	    	 
	     }
	     public void handleNominations(Attributes atts){
	    	 if(currentClientID == clientID){
	    		 nominations = new ArrayList<String>();
	 	 		Log.d(Main.TAG, "found nominations"); 
	 	 		}

	     }
	     public void handleNomination(Attributes atts){
	    	 if(currentClientID == clientID){
	    		 nominations.add(atts.getValue("alias"));
			 		Log.d(Main.TAG, "found nomination"); 
	 	 		} 

	     }

	     public void handleEnrollments(Attributes atts){
	    	 if(currentClientID == clientID){
		    	 enrollments = new ArrayList<LipukaListItem>();
	 	 		} 
	     }
	     public void handleEnrollment(Attributes atts){
		 		Log.d(Main.TAG, "currentClientID: "+clientID); 

	    	 if(currentClientID == clientID){

	    		 LipukaListItem lipukaListItem = new LipukaListItem();
		    		lipukaListItem.setText(atts.getValue("alias"));
		    		lipukaListItem.setValue(atts.getValue("merchant"));
		    		enrollments.add(lipukaListItem);
		    		} 
	    	 
	     }
	     
	     public void handleBankClientID(Attributes atts){
	  
	    	 clientID = Integer.valueOf(atts.getValue("clientID"));
	    	 } 	     
	     public byte getActivityType(){
	    	 return profileDataType;
	     }
	     public List<String> getNominations(){
	    	 return nominations;
	     }
	     public List<LipukaListItem> getEnrollments(){
	    	 return enrollments;
	     }
	    
	     public ArrayList<Bank> getBanks(){
	    	 return banks;
	     }
	     public ArrayList<BankAccount> getBankAccounts(){
	    	 return bankAccounts;
	     }
	     public boolean isPinStatus(){
	    return pinStatus;
	     }
	     
	     public void setCurrentClientID(int id){
	    	 currentClientID = id;
	     }
	 
	}