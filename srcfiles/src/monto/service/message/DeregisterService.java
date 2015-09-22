package monto.service.message;

public class DeregisterService {

    private final String deregisterServiceID;

    public DeregisterService(String deregisterServiceID) {
        this.deregisterServiceID = deregisterServiceID;
    }

    public String getDeregisterServiceID() {
        return deregisterServiceID;
    }
}
