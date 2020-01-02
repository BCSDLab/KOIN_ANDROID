package in.koreatech.koin.core.sharedpreference;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hyerim on 2018. 5. 31....
 */
public class GsonHelper {

    public JSONObject objectToJSON(Object obj) {
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(gson.toJson(obj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public <T> String ArrayListToJSONString(ArrayList<T> arrayList) {
        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
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

        GsonBuilder gsonb = new GsonBuilder();
        Gson gson = gsonb.create();
        return gson.fromJson(json, cls);
    }
}
