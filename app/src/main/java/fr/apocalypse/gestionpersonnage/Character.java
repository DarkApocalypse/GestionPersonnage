package fr.apocalypse.gestionpersonnage;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Character {
	HashMap<String, Integer> fields;
	HashMap<String, Integer> defaults;
	HashMap<String, Integer> min;
	HashMap<String, Integer> max;
	ArrayList<String> orders;
	HashMap<String, String> visibles;
	public Character(){
		fields = new HashMap<>();
		defaults = new HashMap<>();
		min = new HashMap<>();
		max = new HashMap<>();
		orders = new ArrayList<>();
		visibles = new HashMap<>();
	}
	public boolean addField(String name){
		return addField(name,0);
	}
	public boolean addField(String name, int value){
		if(!fields.containsKey(name))
		{
			fields.put(name, Integer.valueOf(value));
			orders.add(name);
			return true;
		}
		return false;
	}
	public boolean setDefault(String name, int value){
		if(fields.containsKey(name)) {
			if(min.containsKey(name) && min.get(name).intValue() > value)
				return false;
			if(max.containsKey(name) && max.get(name).intValue() < value)
				return false;
			defaults.put(name, Integer.valueOf(value));
			return true;
		}
		return false;
	}
	public boolean unsetDefault(String name){
		if(fields.containsKey(name) && defaults.containsKey(name)) {
			defaults.remove(name);
			return true;
		}
		return false;
	}
	public boolean setMin(String name, int value){
		if(fields.containsKey(name)) {
			if(max.containsKey(name) && max.get(name).intValue() < value)
				return false;
			min.put(name, Integer.valueOf(value));
			return true;
		}
		return false;
	}
	public boolean unsetMin(String name){
		if(fields.containsKey(name) && min.containsKey(name)) {
			min.remove(name);
			return true;
		}
		return false;
	}
	public boolean setMax(String name, int value){
		if(fields.containsKey(name)) {
			if(min.containsKey(name) && min.get(name).intValue() > value)
				return false;
			max.put(name, Integer.valueOf(value));
			return true;
		}
		return false;
	}
	public boolean unsetMax(String name){
		if(fields.containsKey(name) && max.containsKey(name)) {
			max.remove(name);
			return true;
		}
		return false;
	}
	public boolean removeField(String name){
		if(fields.containsKey(name)) {
			fields.remove(name);
			Log.d("GESTION-PERSONNAGE", String.format("length before: %d", orders.size()));
			orders.remove(name);
			Log.d("GESTION-PERSONNAGE", String.format("length after: %d", orders.size()));
			if(defaults.containsKey(name)) {
				defaults.remove(name);
			}
			if(min.containsKey(name)) {
				min.remove(name);
			}
			if(max.containsKey(name)) {
				max.remove(name);
			}
			return true;
		}
		return false;
	}
	public boolean addToField(String name, int value){
		if(fields.containsKey(name)) {
			int newValue = fields.get(name).intValue() + value;
			if(min.containsKey(name))
			{
				int min = this.min.get(name).intValue();
				if(newValue < min)
					newValue = min;
			}
			if(max.containsKey(name))
			{
				int max = this.max.get(name).intValue();
				if(newValue > max)
					newValue = max;
			}
			fields.put(name, newValue);
		}
		return false;
	}

	public void loadFromJson(String toString) throws JSONException {
		JSONObject jsonObject = new JSONObject(toString);

		try{
			JSONObject sub = jsonObject.getJSONObject("fields");
			Iterator<String> it = sub.keys();
			while(it.hasNext()){
				String name = it.next();
				addField(name, sub.getInt(name));
			}
			sub = jsonObject.getJSONObject("defaults");
			it = sub.keys();
			while(it.hasNext()){
				String name = it.next();
				setDefault(name, sub.getInt(name));
			}
			sub = jsonObject.getJSONObject("min");
			it = sub.keys();
			while(it.hasNext()){
				String name = it.next();
				setMin(name, sub.getInt(name));
			}
			sub = jsonObject.getJSONObject("max");
			it = sub.keys();
			while(it.hasNext()){
				String name = it.next();
				setMax(name, sub.getInt(name));
			}
			JSONArray jsonArray = jsonObject.getJSONArray("orders");
			orders.clear();
			for(int i = 0; i < jsonArray.length(); i++){
				orders.add((String)jsonArray.get(i));
			}
			sub = jsonObject.getJSONObject("visibles");
			it = sub.keys();
			while(it.hasNext()){
				String name = it.next();
				setVisible(name, sub.getString(name));
			}

		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public String serialize() {
		JSONObject jsonObject = new JSONObject();

		try{
			JSONObject sub = new JSONObject();
			for (Iterator<Map.Entry<String, Integer>> it = fields.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Integer> entry = it.next();

				sub.put(entry.getKey(), entry.getValue());
			}
			jsonObject.put("fields", sub);
			sub = new JSONObject();
			for (Iterator<Map.Entry<String, Integer>> it = defaults.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Integer> entry = it.next();

				sub.put(entry.getKey(), entry.getValue());
			}
			jsonObject.put("defaults", sub);
			sub = new JSONObject();
			for (Iterator<Map.Entry<String, Integer>> it = min.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Integer> entry = it.next();

				sub.put(entry.getKey(), entry.getValue());
			}
			jsonObject.put("min", sub);
			sub = new JSONObject();
			for (Iterator<Map.Entry<String, Integer>> it = max.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, Integer> entry = it.next();

				sub.put(entry.getKey(), entry.getValue());
			}
			jsonObject.put("max", sub);
			jsonObject.put("orders", new JSONArray(orders));
			sub = new JSONObject();
			for (Iterator<Map.Entry<String, String>> it = visibles.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<String, String> entry = it.next();

				sub.put(entry.getKey(), entry.getValue());
			}
			jsonObject.put("visibles", sub);

		}
		catch (Exception e){
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

	public boolean resetField(String name) {
		if(defaults.containsKey(name)) {
			return setField(name, defaults.get(name).intValue());
		}
		return false;
	}

	public boolean setField(String name, int value) {
		if(fields.containsKey(name))
		{
			fields.put(name,value);
			return true;
		}
		return false;
	}

	public boolean hasDefault(String key) {
		return defaults.containsKey(key);
	}
	public Integer getDefault(String key) {
		return defaults.get(key);
	}
	public boolean hasMin(String key) {
		return min.containsKey(key);
	}
	public Integer getMin(String key) {
		return min.get(key);
	}
	public boolean hasMax(String key) {
		return max.containsKey(key);
	}
	public Integer getMax(String key) {
		return max.get(key);
	}
	public boolean hasVisible(String key) {
		return visibles.containsKey(key);
	}
	public String getVisible(String key) {
		return visibles.get(key) + "-";
	}

	public boolean defaultVisible(String key){
		return visibles.containsKey(key) && visibles.get(key).contains("default");
	}
	public boolean minVisible(String key){
		return visibles.containsKey(key) && visibles.get(key).contains("min");
	}
	public boolean maxVisible(String key){
		return visibles.containsKey(key) && visibles.get(key).contains("max");
	}

	public void setVisible(String name, String visible) {
		visibles.put(name, visible);
	}

	public void orderFromJson(String result) {
		orders.clear();

		try {
			JSONArray sub = new JSONArray(result);
			for(int i=0; i < sub.length(); i++){
				orders.add((String)sub.get(i));
			}
		}
		catch (Exception exp){

		}
	}
}
