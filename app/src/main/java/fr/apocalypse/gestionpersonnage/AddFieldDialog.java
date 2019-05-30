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

public class AddFieldDialog extends DialogFragment {

	public AddFieldDialog() {
		super();
		listener = new AddFieldDialogListener() {
			@Override
			public void onDialogPositiveClick(DialogFragment dialog) {

			}

			@Override
			public void onDialogNegativeClick(DialogFragment dialog) {

			}
		};
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.new_field);

		builder.setView(R.layout.add_field)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClick(AddFieldDialog.this);
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(AddFieldDialog.this);
					}
				});
		return builder.create();
	}


	public boolean hasName(){
		EditText edit =  getDialog().findViewById(R.id.edit_name);
		return edit.getText().toString().matches("[a-zA-Z]+( ?[a-zA-Z]+)*");
	}
	public String getName(){
		EditText edit =  getDialog().findViewById(R.id.edit_name);
		return edit.getText().toString();
	}

	public boolean hasDefault(){
		EditText edit =  getDialog().findViewById(R.id.edit_default);
		return edit.getText().toString().matches("-?[0-9]+");
	}
	public int getDefault(){
		EditText edit =  getDialog().findViewById(R.id.edit_default);
		return Integer.parseInt(edit.getText().toString());
	}

	public boolean hasMin(){
		EditText edit =  getDialog().findViewById(R.id.edit_min);
		return edit.getText().toString().matches("-?[0-9]+");
	}
	public int getMin(){
		EditText edit =  getDialog().findViewById(R.id.edit_min);
		return Integer.parseInt(edit.getText().toString());
	}

	public boolean hasMax(){
		EditText edit =  getDialog().findViewById(R.id.edit_max);
		return edit.getText().toString().matches("-?[0-9]+");
	}
	public int getMax(){
		EditText edit =  getDialog().findViewById(R.id.edit_max);
		return Integer.parseInt(edit.getText().toString());
	}

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
	public interface AddFieldDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	AddFieldDialogListener listener;
}
