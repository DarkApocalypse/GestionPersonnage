package fr.apocalypse.gestionpersonnage;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

class DataImageButton extends AppCompatImageButton {
	String data;

	public DataImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public DataImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DataImageButton(Context context) {
		super(context);
	}

	public void setData(String d)
	{
		data = d;
	}
}
