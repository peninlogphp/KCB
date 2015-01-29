package lipuka.android.model.database;

import android.database.Cursor;

public class DatabaseResponse {

	public static final byte ERROR = 0;
	public static final byte OK = 1;
    private byte status;
    private Cursor cursor;
    private String message;
    
    
public void setStatus(byte status){
    	this.status = status;
    } 
public byte getStatus(){
    	return status;
    }
public void setCursor(Cursor cursor){
	this.cursor = cursor;
} 
public Cursor getCursor(){
	return cursor;
}
public String getMessage(){
	return message;
} 
public void setMessage(String message){
	this.message = message;
} 
}
