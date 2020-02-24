package in.koreatech.koin.core.sharedpreference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

public class GsonHelper {

    public JSONObject objectToJSON(Object obj) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public <T> String ArrayListToJSONString(ArrayList<T> arrayList) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject jsonObject = null;
        String jsonText = "";
        try {
            jsonText = gson.toJson(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonText;
    }

    public <T> T jsonToObject(String json, Class<T> cls) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, cls);
    }
}