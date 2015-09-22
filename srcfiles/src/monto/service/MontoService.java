package monto.service;

import monto.service.configuration.Option;
import monto.service.message.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

import java.util.ArrayList;
import java.util.List;

/**
 * Template for a monto service.
 */
public abstract class MontoService implements Runnable {

    private ZContext context;
    private Socket registrationSocket;
    private String address;
    private int port;
    private String registrationAddress;
    private volatile boolean running;

    protected volatile String serviceID;
    protected volatile String label;
    protected volatile String description;
    protected volatile Language language;
    protected volatile Product product;
    protected volatile Option[] options;
    protected volatile String[] dependencies;

    /**
     * Template for a monto service.
     * @param context
     * @param address address of the service without port, e.g. "tcp://*"
     * @param registrationAddress registration address of the broker, e.g. "tcp://*:5004"
     * @param serviceID
     * @param product
     * @param language
     * @param dependencies
     */
    public MontoService(ZContext context, String address, String registrationAddress, String serviceID, String label, String description, Product product, Language language, String[] dependencies) {
        this.context = context;
        this.address = address;
        this.registrationAddress = registrationAddress;
        this.serviceID = serviceID;
        this.label = label;
        this.description = description;
        this.language = language;
        this.product = product;
        this.options = null;
        this.dependencies = dependencies;
        running = true;
    }

    /**
     * Template for a monto service with options.
     * @param context
     * @param address address of the service without port, e.g. "tcp://*"
     * @param registrationAddress registration address of the broker, e.g. "tcp://*:5004"
     * @param serviceID
     * @param label
     * @param description
     * @param language
     * @param product
     * @param options
     * @param dependencies
     */
    public MontoService(ZContext context, String address, String registrationAddress, String serviceID, String label, String description, Language language, Product product, Option[] options, String[] dependencies) {
        this(context, address, registrationAddress, serviceID, label, description, product, language, dependencies);
        this.options = options;
    }

    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        registerService();
        if (isRegisterResponseOk()) {
            Socket socket = connectService();
            while (running) {
                try {
                    handleMessages(socket);
                    Thread.sleep(1);
                } catch (ZMQException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop() {
        running = false;
        System.out.println("disconnecting: " + serviceID);
        System.out.println("deregistering: " + serviceID);
        registrationSocket.send(RegisterMessages.encode(new DeregisterService(serviceID)).toJSONString());
        System.out.println("terminated: " + serviceID);
    }

    private void registerService() {
        System.out.println("registering: " + serviceID + " on " + registrationAddress);
        registrationSocket = context.createSocket(ZMQ.REQ);
        registrationSocket.connect(registrationAddress);
        registrationSocket.send(RegisterMessages.encode(new RegisterServiceRequest(serviceID, label, description, language, product, options, dependencies)).toJSONString());
    }

    private boolean isRegisterResponseOk() {
        JSONObject response = (JSONObject) JSONValue.parse(registrationSocket.recvStr());
        RegisterServiceResponse decodedResponse = RegisterMessages.decodeResponse(response);
        if (decodedResponse.getResponse().equals("ok")) {
            port = decodedResponse.getBindOnPort();
            System.out.println("registered: " + serviceID + ", connecting on " + address + ":" + port);
            return true;
        }
        System.out.printf("could not register service %s: %s%n", serviceID, decodedResponse.getResponse());
        return false;
    }

    private Socket connectService() {
        Socket socket = context.createSocket(ZMQ.PAIR);
        socket.connect(address + ":" + port);
        System.out.println("connected: " + serviceID);
        return socket;
    }

    private void handleMessages(Socket socket) throws Exception {
        Boolean isConfig = false;
        String rawMsg = socket.recvStr(ZMQ.NOBLOCK);
        if (rawMsg != null && !rawMsg.equals("")) {
            JSONArray messages = (JSONArray) JSONValue.parse(rawMsg);
            List<Message> decodedMessages = new ArrayList<>();
            for (Object object : messages) {
                JSONObject message = (JSONObject) object;
                Message decoded = null;
                if (message.containsKey("product")) {
                    decoded = ProductMessages.decode(message);
                } else if (message.containsKey("configurations")){
                    isConfig = true;
                    decoded = ConfigurationMessages.decode(message);
                } else {
                    decoded = VersionMessages.decode(message);
                }
                if (decoded != null) {
                    decodedMessages.add(decoded);
                }
            }
            if (isConfig) {
                isConfig = false;
                onConfigurationMessage(decodedMessages);
            } else {
                socket.send(ProductMessages.encode(onVersionMessage(decodedMessages)).toJSONString());
            }

        }
    }

    /**
     * This method is called by the run() method.
     * It handles the version messages from the broker and determines the response.
     * @param messages
     * @return
     * @throws Exception
     */
    public abstract ProductMessage onVersionMessage(List<Message> messages) throws Exception;

    /**
     * This method is called by the run() method.
     * It handles the configuration messages from the broker and determines the response.
     * @param messages
     * @return
     * @throws Exception
     */
    public abstract void onConfigurationMessage(List<Message> messages) throws Exception;

    public String getServiceID() {
        return serviceID;
    }

    public Language getLanguage() {
        return language;
    }

    public Product getProduct() {
        return product;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public Option[] getOptions() {
        return options;
    }
}
