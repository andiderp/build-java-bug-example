package monto.service.configuration;

public class Configuration<T> {

    private String optionID;
    private T value;

    public Configuration(String optionID, T value) {
        this.optionID = optionID;
        this.value = value;
    }

    public String getOptionID() {
        return optionID;
    }

    public T getValue() {
        return value;
    }
}
