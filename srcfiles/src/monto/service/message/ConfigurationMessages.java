package monto.service.message;

import org.json.simple.JSONObject;

public class ConfigurationMessages {

    @SuppressWarnings("unchecked")
    public static ConfigurationMessage decode(JSONObject message) throws ParseException {
        try {
            final String serviceID = (String) message.get("service_id");
            final String configurations = (String) message.get("configurations");
            return new ConfigurationMessage(serviceID, configurations);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }
}
