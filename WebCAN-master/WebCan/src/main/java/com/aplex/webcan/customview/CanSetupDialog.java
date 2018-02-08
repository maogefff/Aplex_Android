package com.aplex.webcan.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.aplex.webcan.R;
import com.aplex.webcan.flatui.view.FlatButton;

public class CanSetupDialog extends Dialog{

	public CanSetupDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public CanSetupDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public CanSetupDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	public static class Builder { 
		private CanSetupDialog mCanSetupDialog;
		
        private Context context;  
        private String title;  
        private String message;  
        private String positiveButtonText;  
        private String negativeButtonText;

        private View contentView;  
        private DialogInterface.OnClickListener positiveButtonClickListener;  
        private DialogInterface.OnClickListener negativeButtonClickListener;  
  
        private String spinner1Text;
        private String spinner2Text;
        private int sipnner1SelectedPosition;
        private int sipnner2SelectedPosition;
        private String[] spinner1Data;
        private String[] spinner2Data;
        
        private OnItemSelectedListener mSpinnerItemSelectedListener1;
        private OnItemSelectedListener mSpinnerItemSelectedListener2;
        
        public Builder(Context context) {  
            this.context = context;  
        }  
  
        public Builder setMessage(String message) {  
            this.message = message;  
            return this;  
        }  
  
        /** 
         * Set the Dialog message from resource 
         *  
         * @param title 
         * @return 
         */  
        public Builder setMessage(int message) {  
            this.message = (String) context.getText(message);  
            return this;  
        }  
  
        /** 
         * Set the Dialog title from resource 
         *  
         * @param title 
         * @return 
         */  
        public Builder setTitle(int title) {  
            this.title = (String) context.getText(title);  
            return this;  
        }  
  
        /** 
         * Set the Dialog title from String 
         *  
         * @param title 
         * @return 
         */  
  
        public Builder setTitle(String title) {  
            this.title = title;  
            return this;  
        }  
  
        public Builder setContentView(View v) {  
            this.contentView = v;  
            return this;  
        }  
  
        /** 
         * Set the positive button resource and it's listener 
         *  
         * @param positiveButtonText 
         * @return 
         */  
        public Builder setPositiveButton(int positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = (String) context  
                    .getText(positiveButtonText);  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setPositiveButton(String positiveButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.positiveButtonText = positiveButtonText;  
            this.positiveButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(int negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = (String) context  
                    .getText(negativeButtonText);  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
  
        public Builder setNegativeButton(String negativeButtonText,  
                DialogInterface.OnClickListener listener) {  
            this.negativeButtonText = negativeButtonText;  
            this.negativeButtonClickListener = listener;  
            return this;  
        }  
        /**************************	  添加 dismiss   *********************************************************/
        public void dismiss(){
        	if (mCanSetupDialog == null) {
				throw new RuntimeException("Dialog was not initialized");
			}
        	mCanSetupDialog.dismiss();
        }
        /**************************添加spinner start********************************************************/
        public Builder setSpinner1(String key, String[] values,int selectedposition,OnItemSelectedListener listener){
        	this.spinner1Text = key;
        	this.spinner1Data = values;
        	this.sipnner1SelectedPosition = selectedposition;
        	this.mSpinnerItemSelectedListener1 = listener;
        	return this;
        }
        
        public Builder setSpinner2(String key, String[] values,int selectedposition, OnItemSelectedListener listener){
        	this.spinner2Text = key;
        	this.spinner2Data = values;
        	this.sipnner2SelectedPosition = selectedposition;
        	this.mSpinnerItemSelectedListener2 = listener;
        	return this;
        }
       
        /**************************添加spinner  end********************************************************/
        public CanSetupDialog create() {  
            LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            // instantiate the dialog with the custom Theme  
            final CanSetupDialog dialog = new CanSetupDialog(context,R.style.Dialog);  
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);  
            dialog.addContentView(layout, new LayoutParams(  
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));  
            // set the dialog title  
            ((TextView) layout.findViewById(R.id.title)).setText(title);  
            // set the confirm button  
            if (positiveButtonText != null) {  
                ((FlatButton) layout.findViewById(R.id.positiveButton))  
                        .setText(positiveButtonText);  
                if (positiveButtonClickListener != null) {  
                    ((FlatButton) layout.findViewById(R.id.positiveButton))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    positiveButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_POSITIVE);  
                                }  
                            });  
                }  
            } else {  
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.positiveButton).setVisibility(  
                        View.GONE);  
            }  
            // set the cancel button  
            if (negativeButtonText != null) {  
                ((FlatButton) layout.findViewById(R.id.negativeButton))  
                        .setText(negativeButtonText);  
                if (negativeButtonClickListener != null) {  
                    ((FlatButton) layout.findViewById(R.id.negativeButton))  
                            .setOnClickListener(new View.OnClickListener() {  
                                public void onClick(View v) {  
                                    negativeButtonClickListener.onClick(dialog,  
                                            DialogInterface.BUTTON_NEGATIVE);  
                                }  
                            });  
                }  
            } else {  
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.negativeButton).setVisibility(  
                        View.GONE);  
            }  
            if (spinner1Text != null && spinner1Data != null) { 
            	((TextView) layout.findViewById(R.id.spinner1_text))  
                .setText(spinner1Text);
            	Spinner spinner1 = ((Spinner) layout.findViewById(R.id.spinner_1));
            	ArrayAdapter<String> spinner1Adapter = new ArrayAdapter<String>(
        				context, R.layout.spinner_button, spinner1Data);
            	spinner1Adapter.setDropDownViewResource(R.layout.simple_flat_list_item);
            	spinner1.setAdapter(spinner1Adapter);
            	spinner1.setSelection(sipnner1SelectedPosition);
		        if (mSpinnerItemSelectedListener1 != null) {  
		            spinner1.setOnItemSelectedListener(mSpinnerItemSelectedListener1);
		        } 
            }else {
            	layout.findViewById(R.id.spinner1_container).setVisibility(  
                        View.GONE); 
			}
            if (spinner2Text != null && spinner2Data != null) { 
            	((TextView) layout.findViewById(R.id.spinner2_text))  
                .setText(spinner2Text);
            	Spinner spinner2 = ((Spinner) layout.findViewById(R.id.spinner_2));
            	ArrayAdapter<String> spinner2Adapter = new ArrayAdapter<String>(
        				context, R.layout.spinner_button, spinner2Data);
            	spinner2Adapter.setDropDownViewResource(R.layout.simple_flat_list_item);
            	spinner2.setAdapter(spinner2Adapter);
            	spinner2.setSelection(sipnner2SelectedPosition);
		        if (mSpinnerItemSelectedListener2 != null) {  
		            spinner2.setOnItemSelectedListener(mSpinnerItemSelectedListener2);
		        } 
            }else {
            	layout.findViewById(R.id.spinner2_container).setVisibility(  
                        View.GONE); 
			}
            // set the content message  
            if (message != null) {  
                ((TextView) layout.findViewById(R.id.title)).setText(message);  
            } else if (contentView != null) { 
            	layout.findViewById(R.id.title).setVisibility(  
                        View.GONE); 
                // if no message set  
                // add the contentView to the dialog body  
//                ((LinearLayout) layout.findViewById(R.id.content))  
//                        .removeAllViews();  
//                ((LinearLayout) layout.findViewById(R.id.content))  
//                        .addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));  
            }  
            dialog.setContentView(layout);  
            mCanSetupDialog =dialog;
            return dialog;  
        }  
    }  
}
