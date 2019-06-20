package fr.apocalypse.gestionpersonnage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;

public class OrderFields extends AppCompatActivity {
    ArrayList<String> order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order = new ArrayList<>();
        setContentView(R.layout.activity_order_fields);

        Intent intent = getIntent();
        String message = intent.getStringExtra("JSON");

        Character c = new Character();
        try{
            c.loadFromJson(message);
        }
        catch(Exception exp){

        }
        LinearLayout ll = (LinearLayout)findViewById(R.id.fields_list);

        showCharacter(c, ll);

        Button bp_save = (Button)findViewById(R.id.bp_reorder);
        bp_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("order", ((new JSONArray(order)).toString()));
                setResult(1, data);
                finish();
            }
        });
    }

    private void showCharacter(Character c, LinearLayout l) {
        Iterator<String> it = c.orders.iterator();

        while(it.hasNext())
        {
            String entry = it.next();
            LayoutInflater inflater = (LayoutInflater)getBaseContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout main = (LinearLayout)findViewById(R.id.fields_list);
            View view = inflater.inflate(R.layout.field_order, null);
            TextView tv = (TextView)view.findViewById(R.id.field_name);
            DataImageButton bp_up = (DataImageButton)view.findViewById(R.id.bp_up);
            DataImageButton bp_down = (DataImageButton)view.findViewById(R.id.bp_down);

            tv.setText(entry);
            order.add(entry);
            bp_up.setData(entry);
            bp_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout main = (LinearLayout)findViewById(R.id.fields_list);
                    View parent = (View)v.getParent();
                    int i = main.indexOfChild(parent);

                    if(i > 0){
                        main.removeViewAt(i);
                        main.addView(parent, i-1);
                        reorder();
                    }
                }
            });
            bp_down.setData(entry);
            bp_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout main = (LinearLayout)findViewById(R.id.fields_list);
                    View parent = (View)v.getParent();
                    int i = main.indexOfChild(parent);

                    if(i < main.getChildCount()){
                        main.removeViewAt(i);
                        main.addView(parent);
                        reorder();
                    }
                }
            });

            main.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }
    private void reorder(){
        order.clear();
        LinearLayout main = (LinearLayout)findViewById(R.id.fields_list);
        int length = main.getChildCount();
        for(int i = 0; i < length; i++){
            DataImageButton v = main.getChildAt(i).findViewById(R.id.bp_up);
            order.add(v.data);
        }
    }

}
