package fr.apocalypse.gestionpersonnage;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity{

	Character c;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LinearLayout ll = (LinearLayout)findViewById(R.id.list);

		Button bp_add = (Button)findViewById(R.id.bp_add_field) ;
		bp_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DataImageButton self = (DataImageButton)v;

				AddFieldDialog newFragment = new AddFieldDialog();
				newFragment.listener = new AddFieldDialog.AddFieldDialogListener() {
					@Override
					public void onDialogPositiveClick(DialogFragment dialog) {
						AddFieldDialog self = ((AddFieldDialog)dialog);
						if(!self.hasName())
							return;
						String name = self.getName();
						Log.d("dialogListener", "add a field:" + name);

						MainActivity.this.c.addField(name);
						if(self.hasDefault()){
							MainActivity.this.c.setDefault(name, self.getDefault());
						}
						if(self.hasMin()){
							MainActivity.this.c.setMin(name, self.getMin());
						}
						if(self.hasMax()){
							MainActivity.this.c.setMax(name, self.getMax());
						}
						MainActivity.this.c.resetField(name);
						refresh();
					}

					@Override
					public void onDialogNegativeClick(DialogFragment dialog) {

					}
				};
				newFragment.show(getSupportFragmentManager(), "AddFieldDialog");

			}
		});
		Button bp_reset = (Button)findViewById(R.id.bp_reset_all) ;
		bp_reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Iterator<String> it = c.defaults.keySet().iterator();
				while(it.hasNext()){
					String key = it.next();
					c.resetField(key);
				}
			}
		});

		c = loadCharacter();
		showCharacter(c, ll);
	}

	@Override
	protected void onStop() {
		super.onStop();
		String json = c.serialize();

		try{
			FileOutputStream fos = new FileOutputStream(getFilesDir().getAbsolutePath() + "/character-0.json");
			fos.write(json.getBytes());
			fos.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private void showCharacter(Character c, LinearLayout l) {
		Iterator<Map.Entry<String,Integer>> it = c.fields.entrySet().iterator();

		while(it.hasNext())
		{
			Map.Entry<String,Integer> entry = it.next();
			LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout main = (LinearLayout)findViewById(R.id.list);
			View view = inflater.inflate(R.layout.field, null);
			TextView tv = (TextView)view.findViewById(R.id.label);
			DataImageButton bp_add = (DataImageButton) view.findViewById(R.id.bp_add);
			DataImageButton bp_edit = (DataImageButton) view.findViewById(R.id.bp_edit);
			DataImageButton bp_reset = (DataImageButton) view.findViewById(R.id.bp_reset);
			DataImageButton bp_remove = (DataImageButton) view.findViewById(R.id.bp_remove);
			StringBuilder sb = new StringBuilder();
			sb.append(entry.getKey());
			boolean s = false;
			if(c.hasDefault(entry.getKey()))
			{
				sb.append("(D:" + c.getDefault(entry.getKey()).toString());
				s = true;
			}
			if(c.hasMin(entry.getKey()))
			{
				if(s)
					sb.append(", ");
				else
					sb.append("(");
				sb.append("m:" + c.getMin(entry.getKey()).toString());
			}
			if(c.hasMax(entry.getKey()))
			{
				if(s)
					sb.append(", ");
				else
					sb.append("(");
				sb.append("M:" + c.getMax(entry.getKey()).toString());
			}
			if(s)
				sb.append(")");
			sb.append(": " + entry.getValue());
			tv.setText(sb.toString());

			bp_add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DataImageButton self = (DataImageButton)v;

					AddDialog newFragment = new AddDialog();
					newFragment.setName(self.data);
					newFragment.listener = new AddDialog.AddDialogListener() {
						@Override
						public void onDialogPositiveClick(DialogFragment dialog) {
							AddDialog self = ((AddDialog)dialog);
							Log.d("dialogListener", self.getName() + ":" + self.getValue());

							MainActivity.this.c.addToField(self.getName(), self.getValue());
							refresh();
						}

						@Override
						public void onDialogNegativeClick(DialogFragment dialog) {

						}
					};
					newFragment.show(getSupportFragmentManager(), "addDialog");

				}
			});
			bp_add.setData(entry.getKey());

			bp_edit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DataImageButton self = (DataImageButton)v;
					Log.d("CLICKBUTTON", "Click to edit field "+self.data);
					EditDialog newFragment = new EditDialog();
					String[] d = self.data.split("\\|");
					newFragment.setName(d[0]);
					newFragment.loadValues(d[1],d[2],d[3]);
					newFragment.listener = new EditDialog.EditDialogListener() {
						@Override
						public void onDialogPositiveClick(DialogFragment dialog) {
							EditDialog self = ((EditDialog)dialog);

							String name = self.getName();
							int value; boolean good = true;
							Log.d("editDialog", "Start edition");
							if(self.hasDefault()){
								value = self.getDefault();
								Log.d("editDialog", "set default of "+ name + " to "+ value);
								good = MainActivity.this.c.setDefault(name, value);
							}
							else{
								Log.d("editDialog", "unset default of "+ name);
								good = MainActivity.this.c.unsetDefault(name);
							}
							Log.d("editDialog", "is it good?" + (good ? "true" : "false"));
							if(self.hasMin()){
								value = self.getMin();
								Log.d("editDialog", "set min of "+ name + " to "+ value);
								good = MainActivity.this.c.setMin(name, value);
							}
							else{
								Log.d("editDialog", "unset min of "+ name);
								good = MainActivity.this.c.unsetMin(name);
							}
							Log.d("editDialog", "is it good?" + (good ? "true" : "false"));
							if(self.hasMax()){
								value = self.getMax();
								Log.d("editDialog", "set max of "+ name + " to "+ value);
								good = MainActivity.this.c.setMax(name, value);
							}
							else{
								Log.d("editDialog", "unset max of "+ name);
								good = MainActivity.this.c.unsetMax(name);
							}
							Log.d("editDialog", "is it good?" + (good ? "true" : "false"));
							refresh();
						}

						@Override
						public void onDialogNegativeClick(DialogFragment dialog) {

						}
					};
					newFragment.show(getSupportFragmentManager(), "editDialog");
				}
			});
			bp_edit.setData(entry.getKey() + "|" + c.getDefault(entry.getKey()) + "|" + c.getMin(entry.getKey()) + "|" + c.getMax(entry.getKey()));

			bp_reset.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DataImageButton self = (DataImageButton)v;
					Log.d("CLICKBUTTON", "Click to reset field "+self.data);
					MainActivity.this.c.resetField(self.data);
					MainActivity.this.refresh();
				}
			});
			bp_reset.setData(entry.getKey());
			bp_remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					DataImageButton self = (DataImageButton)v;
					Log.d("CLICKBUTTON", "Click to remove field "+self.data);
					MainActivity.this.c.removeField(self.data);
					MainActivity.this.refresh();
				}
			});
			bp_remove.setData(entry.getKey());

			main.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		}
	}

	private void refresh()
	{
		LinearLayout ll = (LinearLayout)findViewById(R.id.list);
		ll.removeAllViews();
		showCharacter(c, ll);
	}

	private Character loadCharacter() {
		Character c = new Character();
		try{
			FileInputStream fis = new FileInputStream(getFilesDir().getAbsolutePath() + "/character-0.json");
			StringBuilder sb = new StringBuilder();
			while(fis.available() > 0){
				byte[] buffer = new byte[1024];
				fis.read(buffer);
				sb.append(new String(buffer, "UTF-8"));
			}
			fis.close();
			c.loadFromJson(sb.toString());
		}
		catch (Exception e){
			e.printStackTrace();
			c.addField("PV");
			c.setDefault("PV", 20);
			c.setMin("PV", 0);
			c.setMax("PV", 20);
		}
		return c;
	}
}
