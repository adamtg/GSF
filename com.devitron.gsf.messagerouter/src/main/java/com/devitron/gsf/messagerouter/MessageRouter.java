package com.devitron.gsf.messagerouter;

import com.devitron.gsf.common.configuration.Configuration;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagerouter.exception.AddressNotRegisteredException;
import com.devitron.gsf.messagerouter.register.Register;
import com.devitron.gsf.messagerouter.register.RegisteredServiceController;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.MessageTransportFactory;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

public class MessageRouter {

    static final String MESSAGE_ROUTER_SERVICE_NAME = "MessageRouter";
    Configuration config;
    MessageTransport mt = MessageTransportFactory.getMessageTransport();
    RegisteredServiceController rsc = RegisteredServiceController.getRegisteredServiceController();

    /**
     * Takes a message, parses the header and send it
     * to the appropriate registered service
     *
     * @param message message to route to the proper service
     * @param json String form of message
     */
    public Message serviceMessage(Message message, String json) {
            Message sendMessage = null;

            if (message.getHeader().getFunction().equals("RegisterService")) {
                sendMessage = Register.registerService(json);
            }

           return message;
    }



    private void sendMessage(Message message, String json) {

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
     *  Main loop
     */
    public void mainLoop() {
        boolean continueLoop = true;

        try {
            mt.setupQueue(config.getGlobal().getSendMessageQueue());
        } catch (MessageTransportIOException e) {
            e.printStackTrace();
            // log errror
            return;
        }


        while (continueLoop) {
            String json = null;
            Message message = null;
            String sendMessageJson = null;
            Message sendMessage = null;

            try {

                json = mt.receive();
                message = (Message)Json.jsonToObject(json, Message.class);

                if (message.getHeader().equals(MESSAGE_ROUTER_SERVICE_NAME)) {

                    sendMessage = serviceMessage(message, json);
                    sendMessageJson = Json.objectToJson(reply);

                } else {
                    sendMessage = message;
                    sendMessageJson = json;
                }

                sendMessage(sendMessage, sendMessageJson);

            } catch (MessageTransportIOException e) {
                e.printStackTrace();
            } catch (UtilitiesJsonParseException e) {
                e.printStackTrace();
            }

        }
    }


}
