package monto.service.configuration;

import monto.service.message.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configurations {

    @SuppressWarnings("unchecked")
    public static List<Configuration> encode(String message) throws ParseException {
        try {
            List<Configuration> configurations = new ArrayList<>();
            JSONArray array = (JSONArray) JSONValue.parse(message);
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                final JSONObject obj = iterator.next();
                configurations.add(new Configuration((String) obj.get("option_id"), obj.get("value")));
            }
            return configurations;
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }
}
