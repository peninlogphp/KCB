package lipuka.android.model;




import kcb.android.LipukaApplication;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;





	 
public class ServiceVersionHandler extends DefaultHandler {	     

	public static final byte DEFAULT = 1;
	public static final byte UPDATE = 2;
	private byte currentState;
	LipukaApplication lipukaApplication;
String currentVersion;
	public ServiceVersionHandler(LipukaApplication lipukaApplication){
		super();
 this.lipukaApplication = lipukaApplication;
 
 }
	 
	 @Override
	    public void startElement(String namespaceURI, String localName,
	            String qName, Attributes atts) throws SAXException {
		 
		 if (localName.equals("servicexml")) {
			 if(currentState == DEFAULT){
	     		handleServiceVersion(atts);
	     		}else{
	     		handleServiceVersionForUpdate(atts);	     			
	     		}
	     	}
		 throw new SAXException("1");	        
	 
	    }
	     
	    @Override
	    public void endElement(String uri, String localName, String qName)throws SAXException {
	         
	    }
	     
	    @Override
	    public void characters(char[] ch, int start, int length) {

	    }
     
	     public void handleServiceVersion(Attributes atts){
	     	    	 lipukaApplication.setServiceVersion(atts.getValue("version"));

	     }
	     public void handleServiceVersionForUpdate(Attributes atts){
currentVersion = atts.getValue("version");
 }
	     public String getServiceVersionForUpdate(){
return currentVersion;
 }     
	     public void setState(byte state){
	    	 currentState = state;
	    	  }     
	}