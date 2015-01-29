package lipuka.android.model;


import kcb.android.LipukaApplication;
import kcb.android.Main;
import kcb.android.R;
import lipuka.android.view.LipukaListItem;
import lipuka.android.view.LipukaNavigateButton;
import lipuka.android.view.LipukaSubmitButton;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;


public class LipukaActionListener implements View.OnClickListener, OnItemClickListener{

	Activity activity;
	private LipukaApplication lipukaApplication;

        public LipukaActionListener(LipukaApplication lipukaApplication,
        		Activity activity){
        this.lipukaApplication = lipukaApplication;
        this.activity = activity;

        }

    public void onClick(View v) {
        if (lipukaApplication.getCurrentActivityType() == ServiceXMLParser.DEFAULT) {
    		Log.d(Main.TAG, "Listener worked");
    		LipukaDefaultActivity.reset();
    		LinearLayout linearLayout = (LinearLayout)activity.findViewById(R.id.main_layout);
 String data = LipukaDefaultActivity.getData(linearLayout);

            if (LipukaDefaultActivity.isValidationError()){
        		Log.d(Main.TAG, "Invalid Input");

            	lipukaApplication.setCurrentDialogTitle("Invalid Input");
    lipukaApplication.setCurrentDialogMsg(LipukaDefaultActivity.getErrorMsgs());
            lipukaApplication.showDialog(Main.DIALOG_ERROR_ID);
            return;
            }
		if (v instanceof LipukaSubmitButton) {
	LipukaSubmitButton lipukaSubmitButton = (LipukaSubmitButton)v;
	Navigation nav = new Navigation();
	nav.setRemote(true);
	lipukaApplication.pushNavigationStack(nav);
    int length = lipukaApplication.getNavigationStack().size();
    Navigation previousNav =  lipukaApplication.getNavigation(length-2);
    previousNav.setPayload(data);
LipukaDefaultActivity.saveData(linearLayout, previousNav.getHashMap());

	if (lipukaSubmitButton.isPIN()){
		if(lipukaApplication.getPin() == null){
    	lipukaApplication.promptPIN();
		}else{
            lipukaApplication.executeRemoteRequest();				
		}
                    }else{
       lipukaApplication.executeRemoteRequest();
    return;
                  }
		} else if (v instanceof LipukaNavigateButton){
			LipukaNavigateButton lipukaNavigateButton = (LipukaNavigateButton)v;
Navigation nav = new Navigation();
nav.setActivity(lipukaNavigateButton.getActivity());
lipukaApplication.pushNavigationStack(nav);
int length = lipukaApplication.getNavigationStack().size();
Navigation previousNav =  lipukaApplication.getNavigation(length-2);
previousNav.setPayload(data);
LipukaDefaultActivity.saveData(linearLayout, previousNav.getHashMap());

lipukaApplication.executeLocalRequest(lipukaNavigateButton.getActivity());

                }

        }
    }


	public void onItemClick(AdapterView<?> parent, View view, int position, 
			long id) {
LipukaList lipukaList = lipukaApplication.getLipukaList();
LipukaListItem lipukaListItem = 
	lipukaApplication.getListItems().get(position);

if (lipukaListItem.getValue().equals("none")){
	return;
}
if (lipukaList.getType().equals("normal")){
	handleListNavigation(lipukaList, position);			
}else{
	
	if (lipukaListItem.getText().equals("Other")){
			lipukaApplication.executeLocalRequest(position);
			Navigation nav = new Navigation();
			nav.setActivity(lipukaListItem.getValue());
			lipukaApplication.pushNavigationStack(nav);
			int length = lipukaApplication.getNavigationStack().size();
			Navigation previousNav =  lipukaApplication.getNavigation(length-2);
			previousNav.setPayload("Other|");
		
	}else{
		handleListNavigation(lipukaList, position);		
		
		}
}	

	}
	public void handleListNavigation(LipukaList lipukaList, int position) {
if (lipukaList.getNavigationType() == LipukaList.NAVIGATION_REMOTE){
	LipukaSubmitButton lipukaSubmitButton = lipukaApplication.getLipukaSubmitButton();
	LipukaListItem lipukaListItem = 
		lipukaApplication.getListItems().get(position);

	Navigation nav = new Navigation();
	nav.setRemote(true);
	lipukaApplication.pushNavigationStack(nav);
	int length = lipukaApplication.getNavigationStack().size();
	Navigation previousNav =  lipukaApplication.getNavigation(length-2);
	previousNav.setPayload(lipukaListItem.getValue()+"|");
	if (lipukaSubmitButton.isPIN()){
		if(lipukaApplication.getPin() == null){
	    	lipukaApplication.promptPIN();
			}else{
	              lipukaApplication.executeRemoteRequest();				
			}
			}else{
              lipukaApplication.executeRemoteRequest();
                    }
}else{

	LipukaNavigateButton lipukaNavigateButton = lipukaApplication.getLipukaNavigateButton();
	//if (lipukaNavigateButton == null){	Log.d(Main.TAG, "it s null");}
	if (lipukaNavigateButton.getActivity().length() == 0){
		LipukaListItem lipukaListItem = 
			lipukaApplication.getListItems().get(position);
		
		if(lipukaListItem.getValue().equals("balance")){
			LipukaSubmitButton lipukaSubmitButton = new LipukaSubmitButton(activity);
			lipukaSubmitButton.setAction("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/balance11.php");
			lipukaSubmitButton.setBearer(lipukaSubmitButton.BEARER_HTTPS);
			lipukaSubmitButton.setMethod(LipukaSubmitButton.METHOD_POST);
			lipukaApplication.setLipukaSubmitButton(lipukaSubmitButton);
			if(lipukaApplication.getPin() == null){
		    	lipukaApplication.promptPIN();
				}else{
		              lipukaApplication.executeRemoteRequest();				
				}
			
		}else if(lipukaListItem.getValue().equals("ministmt")){
			LipukaSubmitButton lipukaSubmitButton = new LipukaSubmitButton(activity);
			lipukaSubmitButton.setAction("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/ministmt11.php");
			lipukaSubmitButton.setBearer(lipukaSubmitButton.BEARER_HTTPS);
			lipukaSubmitButton.setMethod(LipukaSubmitButton.METHOD_POST);
			lipukaApplication.setLipukaSubmitButton(lipukaSubmitButton);
			if(lipukaApplication.getPin() == null){
		    	lipukaApplication.promptPIN();
				}else{
		              lipukaApplication.executeRemoteRequest();				
				}
			
		}else if(lipukaListItem.getValue().equals("chequebk")){
			LipukaSubmitButton lipukaSubmitButton = new LipukaSubmitButton(activity);
			lipukaSubmitButton.setAction("http://zion.cellulant.com/AndroidWallet/ECOBANK/processorScripts/chequebk11.php");
			lipukaSubmitButton.setBearer(lipukaSubmitButton.BEARER_HTTPS);
			lipukaSubmitButton.setMethod(LipukaSubmitButton.METHOD_POST);
			lipukaApplication.setLipukaSubmitButton(lipukaSubmitButton);
			if(lipukaApplication.getPin() == null){
		    	lipukaApplication.promptPIN();
				}else{
		              lipukaApplication.executeRemoteRequest();				
				}
			
		}else{
			Navigation nav = new Navigation();
			nav.setActivity(lipukaListItem.getValue());
			lipukaApplication.pushNavigationStack(nav);
			lipukaApplication.executeLocalRequest(position);			
		}
	}else{

		LipukaListItem lipukaListItem = 
			lipukaApplication.getListItems().get(position);
lipukaApplication.executeLocalRequest(lipukaNavigateButton.getActivity());
Navigation nav = new Navigation();
nav.setActivity(lipukaNavigateButton.getActivity());
lipukaApplication.pushNavigationStack(nav);
int length = lipukaApplication.getNavigationStack().size();
Navigation previousNav =  lipukaApplication.getNavigation(length-2);
if(lipukaList.getSource().equals("enrollments")){
previousNav.setPayload(lipukaListItem.getValue()+"|"+lipukaListItem.getText()+"|");
}else if (lipukaList.getSource().equals("accounts")){
	previousNav.setPayload(lipukaListItem.getValue()+"|"+lipukaListItem.getText()+"|");
}else if (lipukaList.getSource().equals("myaccounts")){
	previousNav.setPayload(lipukaListItem.getValue()+"|"+lipukaListItem.getText()+"|");
}else{
	previousNav.setPayload(lipukaListItem.getValue()+"|");	
}
	}
}	
	
	}
}
