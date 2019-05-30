package fr.apocalypse.gestionpersonnage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EditDialog extends DialogFragment {
	public static final String ARG_NAME = "EditDialog.name";
	private String name, defaults, min, max;

	public EditDialog() {
		super();
		this.name = "[null]";
		listener = new EditDialogListener() {
			@Override
			public void onDialogPositiveClick(DialogFragment dialog) {

			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};
		defaults = "";
		min = "";
		max = "";
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(name);

		LayoutInflater inflater = (LayoutInflater)getActivity().getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.edit, null);
		EditText edit;

		edit = (EditText)view.findViewById(R.id.edit_default);
		edit.setText(defaults);
		edit = (EditText)view.findViewById(R.id.edit_min);
		edit.setText(min);
		edit = (EditText)view.findViewById(R.id.edit_max);
		edit.setText(max);

		builder.setView(view)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClick(EditDialog.this);
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(EditDialog.this);
					}
				});
		return builder.create();
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void loadValues(String defaults, String min, String max){
		if(defaults!=null)
			this.defaults = defaults;
		if(min!=null)
			this.min = min;
		if(max!=null)
			this.max = max;
	}

	public boolean hasDefault(){
		EditText edit =  getDialog().findViewById(R.id.edit_default);
		return edit.getText().toString().matches("-?[0-9]+");
	}
	public void setDefault(int value){
		EditText edit =  getDialog().findViewById(R.id.edit_default);
		edit.setText(String.valueOf(value));
	}
	public int getDefault(){
		EditText edit =  getDialog().findViewById(R.id.edit_default);
		return Integer.parseInt(edit.getText().toString());
	}

	public boolean hasMin(){
		EditText edit =  getDialog().findViewById(R.id.edit_min);
		return edit.getText().toString().matches("-?[0-9]+");
	}
	public void setMin(int value){
		EditText edit =  getDialog().findViewById(R.id.edit_min);
		edit.setText(String.valueOf(value));
	}
	public int getMin(){
		EditText edit =  getDialog().findViewById(R.id.edit_min);
		return Integer.parseInt(edit.getText().toString());
	}

	public boolean hasMax(){
		EditText edit =  getDialog().findViewById(R.id.edit_max);
		return edit.getText().toString().matches("-?[0-9]+");
	}
	public void setMax(int value){
		EditText edit =  getDialog().findViewById(R.id.edit_max);
		edit.setText(String.valueOf(value));
	}
	public int getMax(){
		EditText edit =  getDialog().findViewById(R.id.edit_max);
		return Integer.parseInt(edit.getText().toString());
	}

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
	public interface EditDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	EditDialogListener listener;
}
