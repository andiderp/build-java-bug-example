package monto.service.message;

import monto.service.configuration.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class RegisterMessages {

    @SuppressWarnings("unchecked")
    public static JSONObject encode(RegisterServiceRequest message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service_id", message.getServiceID());
        jsonObject.put("label", message.getLabel());
        jsonObject.put("description", message.getDescription());
        jsonObject.put("language", message.getLanguage().toString());
        jsonObject.put("product", message.getProduct().toString());
        Option[] options = message.getOptions();
        if (options != null && !(options.length == 0)) {
            JSONArray jsonOptions = new JSONArray();
            for (Option option : options) {
                jsonOptions.add(option.encode());
            }
            jsonObject.put("options", jsonOptions.toJSONString());
        }
        JSONArray dependencies = new JSONArray();
        for (String dependency : message.getDependencies()) {
            dependencies.add(dependency);
        }
        jsonObject.put("dependencies", dependencies);
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject encode(DeregisterService message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deregister_service_id", message.getDeregisterServiceID());
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    public static RegisterServiceResponse decodeResponse(JSONObject message) {
        final String response = (String) message.get("response");
        final int bindOnPort = ((Long) message.getOrDefault("bind_on_port", 0)).intValue();
        return new RegisterServiceResponse(response, bindOnPort);
    }
}
