package lipuka.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class LipukaSubmitButton extends Button implements Navigate{

	public final static byte METHOD_POST = 1;
	public final static byte METHOD_GET = 2;
	public final static byte METHOD_PUT = 3;
	public final static byte METHOD_DELETE = 4;

	public final static byte BEARER_HTTP = 1;
	public final static byte BEARER_HTTPS = 2;
	public final static byte BEARER_SMS = 3;
	public final static byte BEARER_USSD = 4;
	
	private String action;
	private byte method;
	private byte bearer;
	private boolean pin;
	
	public LipukaSubmitButton(Context context) {
		super(context);
		//setBackgroundResource(R.drawable.lipuka_button);
		
//setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}
	public LipukaSubmitButton(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		}
	public LipukaSubmitButton(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;		
	}

	public byte getMethod() {
		return method;
	}

	public void setMethod(byte method) {
this.method = method;		
	}

	public byte getBearer() {
		return bearer;
	}

	public void setBearer(byte bearer) {
		this.bearer = bearer;		
	}
	public boolean isPIN() {
		return pin;
	}

	public void setPIN(boolean pin) {
		this.pin = pin;		
	}
	
}
