package monto.service.configuration;

import org.json.simple.JSONObject;

public class TextOption extends AbstractOption<String> {

    private String regularExpression;

    public TextOption(String optionId, String label, String defaultValue, String regularExpression) {
        super(optionId, label, "text", defaultValue);
        this.regularExpression = regularExpression;
    }

    public String getRegularExpression() {
        return regularExpression;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject encode() {
        JSONObject jsonObject = super.encode();
        jsonObject.put("regular_expression", getRegularExpression());
        return jsonObject;
    }
}
