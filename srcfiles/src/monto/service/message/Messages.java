package monto.service.message;

import java.util.List;


public class Messages {
    public static VersionMessage getVersionMessage(List<Message> messages) {
        if (messages == null)
            throw new IllegalArgumentException("Message list was null");
        VersionMessage versionMessage = messages.stream()
                .filter(msg -> msg instanceof VersionMessage)
                .findFirst()
                .map(msg -> {
                    return (VersionMessage) msg;
                }).get();
        if (versionMessage == null) {
            throw new IllegalArgumentException("VersionMessage missing");
        }
        return versionMessage;
    }

    public static ProductMessage getProductMessage(List<Message> messages, Product product, Language language) {
        if (messages == null)
            throw new IllegalArgumentException("Message list was null");
        ProductMessage productMessage = messages.stream()
                .filter(msg -> {
                    if (msg instanceof ProductMessage) {
                        ProductMessage msg1 = (ProductMessage) msg;
                        return msg1.getProduct().equals(product) && msg1.getLanguage().equals(language);
                    } else {
                        return false;
                    }
                }).findAny()
                .map(msg -> (ProductMessage) msg).get();
        if (productMessage == null) {
            throw new IllegalArgumentException(String.format("ProductMessage missing: %s, %s", product, language));
        }
        return productMessage;
    }

    public static ConfigurationMessage getConfigurationMessage(List<Message> messages) {
        if (messages == null) {
            throw new IllegalArgumentException("Message list was null");
        }
        ConfigurationMessage configurationMessage = messages.stream()
                .filter(msg -> {
                    if (msg instanceof  ConfigurationMessage) {
                        return true;
                    } else {
                        return false;
                    }
                }).findAny()
                .map(msg -> (ConfigurationMessage) msg).get();
        if (configurationMessage == null) {
            throw new IllegalArgumentException("ConfigurationMessage missing");
        }
        return configurationMessage;
    }
}
