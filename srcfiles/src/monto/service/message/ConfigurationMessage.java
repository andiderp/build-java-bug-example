package monto.service.message;

import monto.service.configuration.Configuration;

public class ConfigurationMessage implements Message {

    private String serviceID;
    private String configurations;

    public ConfigurationMessage(String serviceID, String configurations) {
        this.serviceID = serviceID;
        this.configurations = configurations;
    }

    public String getServiceID() {
        return serviceID;
    }

    public String getConfigurations() {
        return configurations;
    }
}
