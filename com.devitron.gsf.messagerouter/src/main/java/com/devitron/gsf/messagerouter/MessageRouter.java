package com.devitron.gsf.messagerouter;

import com.devitron.gsf.common.configuration.Configuration;
import com.devitron.gsf.common.configuration.exceptions.ConfigFileNotFoundException;
import com.devitron.gsf.common.configuration.exceptions.ConfigFileParseException;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagerouter.exception.AddressNotRegisteredException;
import com.devitron.gsf.messagerouter.register.Register;
import com.devitron.gsf.messagerouter.register.RegisteredServiceController;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.MessageTransportFactory;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportInitException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

public class MessageRouter {

    static final String MESSAGE_ROUTER_SERVICE_NAME = "MessageRouter";
    static final String CONFIG_FILENAME = "messageRouterConfig.conf";

    Configuration config;
    static MessageTransport mt = null;
    static RegisteredServiceController rsc = null;


    static public void main(String[] argv) {


        System.out.println("------------------------------------");
        System.out.println("Starting MessageRouter");
        System.out.println("------------------------------------");

        MessageRouter mr = new MessageRouter();

        mr.mt = MessageTransportFactory.getMessageTransport();
        mr.rsc = RegisteredServiceController.getRegisteredServiceController();

        try {
            mr.loadConfig();
        } catch (ConfigFileNotFoundException | ConfigFileParseException e) {
            e.printStackTrace();
            // log
            return;
        }


        System.out.println("address: " + mr.config.getGlobal().getMessageBrokerAddress());
        System.out.println("port: " + mr.config.getGlobal().getMessageBrokerPort());
        System.out.println("user: " + mr.config.getGlobal().getMessageBrokerUsername());
        System.out.println("password: " + mr.config.getGlobal().getMessageBrokerPassword());
        System.out.println("queue: " + mr.config.getGlobal().getSendMessageQueue());


        try {
            mr.mt.init(mr.config.getGlobal().getMessageBrokerAddress(), mr.config.getGlobal().getMessageBrokerPort(),
                    mr.config.getGlobal().getMessageBrokerUsername(), mr.config.getGlobal().getMessageBrokerPassword());
            mr.mt.setupQueue(mr.config.getGlobal().getSendMessageQueue());
            mr.mt.setReceiveQueueDefault(mr.config.getGlobal().getSendMessageQueue());
        } catch (MessageTransportIOException | MessageTransportInitException e) {
            e.printStackTrace();
            // log errror
            return;
        }

        mr.mainLoop();

    }


    public void loadConfig() throws ConfigFileNotFoundException, ConfigFileParseException {

        System.out.println("Going to get config");
        config = Configuration.loadConfiguration(CONFIG_FILENAME, MessageRouterConfiguration.class);
    }

    /**
     * Takes a message, parses the header and send it
     * to the appropriate registered service
     *
     * @param message message to route to the proper service
     * @param json    String form of message
     */
    static public Message serviceMessage(Message message, String json) {
        Message sendMessage = null;

        if (message.getHeader().getFunction().equals("RegisterService")) {
            sendMessage = Register.registerService(json);
        }

        return sendMessage;
    }


    static private void sendMessage(Message message, String json) {

        String queueName = null;
        try {
            queueName = rsc.getQueueAddress(message.getHeader().getDestination());
        } catch (AddressNotRegisteredException e) {
            e.printStackTrace();
            return;
        }

        try {
            mt.send(json, queueName);
        } catch (MessageTransportIOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Main loop
     */
    public void mainLoop() {

        try {
            mt.receive(MessageRouter::receiveMessage);

        } catch (MessageTransportIOException e) {
            e.printStackTrace();
        }


    }


    static public void receiveMessage(String json) {

        Message message;
        String sendMessageJson = null;
        Message sendMessage = null;

        System.out.println("--------------------------------------");
        System.out.println(json);
        System.out.println("--------------------------------------");

        try {

            message = (Message) Json.jsonToObject(json, Message.class);

            if (message.getHeader().equals(MESSAGE_ROUTER_SERVICE_NAME)) {

                sendMessage = serviceMessage(message, json);
                sendMessageJson = Json.objectToJson(sendMessage);

            } else {
                sendMessage = message;
                sendMessageJson = json;
            }

            sendMessage(sendMessage, sendMessageJson);


        } catch (UtilitiesJsonParseException e) {
            e.printStackTrace();
        }


    }


}
