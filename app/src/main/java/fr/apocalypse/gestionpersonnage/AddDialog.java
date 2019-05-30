package fr.apocalypse.gestionpersonnage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

public class AddDialog extends DialogFragment {
	public static final String ARG_NAME = "AddDialog.name";
	private String name;

	public AddDialog() {
		super();
		this.name = "[null]";
		listener = new AddDialog.AddDialogListener(){
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
		builder.setTitle(name);
		builder.setView(R.layout.add)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogPositiveClick(AddDialog.this);
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						listener.onDialogNegativeClick(AddDialog.this);
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
	public int getValue(){
		EditText edit =  getDialog().findViewById(R.id.edit_value);
		return Integer.parseInt(edit.getText().toString());
	}

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
	public interface AddDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	AddDialog.AddDialogListener listener;
}
