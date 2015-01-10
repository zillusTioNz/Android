package com.example.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class Dialog extends DialogFragment {

	private dialogDoneListener mListener;
	
	public static Dialog newInstance(){
		Dialog d = new Dialog();
		return d;
	}
	
	@Override
	public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onDone();
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   //mListener.onDone();
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
	}

	public interface dialogDoneListener{
		void onDone();
	}
	
	public void onAttach(Activity activity) {
		 super.onAttach(activity);
		 try {
			 mListener = (dialogDoneListener) activity;
		 } catch (ClassCastException e) {
			 
		 }
	}
}
