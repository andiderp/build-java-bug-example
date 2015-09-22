package monto.service.configuration;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;

public class XorOption extends AbstractOption<String> {

    private List<String> values;

    public XorOption(String optionId, String label, String defaultValue, List<String> values) {
        super(optionId, label, "xor", defaultValue);
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject encode() {
        JSONObject jsonObject = super.encode();
        JSONArray values = new JSONArray();
        for (String value : getValues()) {
            values.add(value);
        }
        jsonObject.put("values", values);
        return jsonObject;
    }
}
