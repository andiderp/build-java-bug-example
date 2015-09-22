package monto.service.configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class OptionGroup implements Option {

    private String requiredOption;
    private Option[] members;

    public OptionGroup(String requiredOption, Option[] members) {
        this.requiredOption = requiredOption;
        this.members = members;
    }

    public String getRequiredOption() {
        return requiredOption;
    }

    public Option[] getMembers() {
        return members;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject encode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("required_option", getRequiredOption());
        JSONArray members = new JSONArray();
        for (Option conf : getMembers()) {
            members.add(conf.encode());
        }
        jsonObject.put("members", members);
        return jsonObject;
    }
}
