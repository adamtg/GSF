package com.devitron.gsf.service;

import com.devitron.gsf.common.configuration.Configuration;
import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.MessageTransportFactory;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportTimeoutException;
import com.devitron.gsf.service.exceptions.ServiceMessageReplyTimeoutException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.Utilities;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public abstract class Service {

    Configuration config;
    MessageTransport mt = null;

    private String queueName = null;

    abstract public Address getAddress();
    abstract public String getServiceName();


    /**
     * Sets up the connection to the message broker, including
     * registering with the MessageRouter
     *
     * @return if service should shutdown
     * @throws MessageTransportIOException
     * @throws MessageTransportTimeoutException
     * @throws UtilitiesJsonParseException
     * @throws ServiceMessageReplyTimeoutException
     */
    public boolean setupMessaging() throws MessageTransportIOException, MessageTransportTimeoutException, UtilitiesJsonParseException, ServiceMessageReplyTimeoutException {

        mt = MessageTransportFactory.getMessageTransport();
        mt.init(config.getGlobal().getMessageBrokerAddress(), config.getGlobal().getMessageBrokerPort(),
                config.getGlobal().getMessageBrokerUsername(), config.getGlobal().getMessageBrokerPassword());

        boolean shutdown = register();

        if (shutdown) {
            shutdownMessageBrokerConnection();
        }

        return shutdown;
    }

    /**
     * Registers service with MessageRouter
     *
     * @return if service should shutdown
     * @throws UtilitiesJsonParseException
     * @throws ServiceMessageReplyTimeoutException
     */
    private boolean register() throws UtilitiesJsonParseException, ServiceMessageReplyTimeoutException {

        boolean shutdown = false;

        com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceRequest regRequest =
                new com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceRequest(getAddress());

        regRequest.shutdownOnDup = config.getGlobal().getShutdownOnDup();
        regRequest.shutdownOnUnique = config.getGlobal().getShutdownOnUnique();
        regRequest.randomeString = Utilities.generateRandomString(5);

        Address serviceAddress = getAddress();
        queueName = serviceAddress.getName() + "_" + serviceAddress.getVersion() + "_" + regRequest.randomeString;

        mt.setupQueue(queueName);



            String jsonReply = send(regRequest, 0);

            com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply regReply =
                    (com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply) Json.jsonToObject(jsonReply, com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply.class
                    );

            shutdown = regReply.shutdown;


        return shutdown;
    }


    /**
     * Sends out a message asynchronously.  The message reply is
     * handled by the callback.  If callback is NULL, the message
     * will be sent, and the reply will be silently tossed.
     *
     * @param message Message to be sent
     * @param callback Function to handle message reply
     */
    public void send(Message message, Function<Message, Message> callback) {
        message.getHeader().setSource(getAddress());
        String json = Json.objectToJson(message);

    }

    /**
     * Sends out a message asynchronously.
     *
     * @param message Message to be sent
     */
    public void send(Message message) {
        message.getHeader().setSource(getAddress());
        String json = Json.objectToJson(message);

        boolean success = mt.send(json);
        if (!success) {
            // log a critical error
        }
    }

    /**
     * Sends out a message synchronously.  If a reply
     * is net received with timeout microseconds, an
     * exception is thrown.
     *
     * A timeout of 0 means to wait indefinitely for
     * the reply
     *
     * @param message Message to be sent
     * @param timeout How long to wait for reply in microseconds
     * @return Reply message
     */
    public String send(Message message, int timeout) throws ServiceMessageReplyTimeoutException {
        message.getHeader().setSource(getAddress());
        message.getHeader().setSync(true);

        return null;
    }

    /**
     * Retrieve a message from the service's queue
     *
     * @return message from the queue
     */
    public Message receiveMessage() {
        return new Message();
    }


    /**
     * Removes queue and shutdown message broker connection
     */
    public void shutdownMessageBrokerConnection() {

        // remove messaging queue
        // close connection to messaging queue

    }



}
