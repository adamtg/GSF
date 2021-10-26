package com.devitron.gsf.service;

import com.devitron.gsf.common.configuration.Configuration;
import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.MessageTransportFactory;
import com.devitron.gsf.service.exceptions.MessageReplyTimeoutException;
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
     * resgistering with the MessageRouter
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public void setupMessageQueue() throws IOException, TimeoutException {

        mt = MessageTransportFactory.getMessageTransport();
        mt.init(config.getGlobal().getMessageBrokerAddress(), config.getGlobal().getMessageBrokerPort(),
                config.getGlobal().getMessageBrokerUsername(), config.getGlobal().getMessageBrokerPassword());

        boolean shutdown = register();

        if (shutdown) {
            // do the shutdown process

        }


    }

    public boolean register() {

        boolean shutdown = false;

        com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceRequest regRequest =
                new com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceRequest(getAddress());

        regRequest.shutdownOnDup = config.getGlobal().getShutdownOnDup();
        regRequest.shutdownOnUnique = config.getGlobal().getShutdownOnUnique();
        regRequest.randomeString = Utilities.generateRandomString(5);

        Address serviceAddress = getAddress();
        queueName = serviceAddress.getName() + "_" + serviceAddress.getVersion() + "_" + regRequest.randomeString;

        mt.setupQueue(queueName);

        try {

            String jsonReply = send(regRequest, 0);

            com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply regReply =
                    (com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply) Json.jsonToObject(jsonReply, com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply.class
                    );

            shutdown = regReply.shutdown;

        } catch (UtilitiesJsonParseException | MessageReplyTimeoutException e) {
            e.printStackTrace();
            shutdown = false;
        }

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

    }

    /**
     * Sends out a message asynchronously.
     *
     * @param message Message to be sent
     */
    public void send(Message message) {
        message.getHeader().setSource(getAddress());

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
    public String send(Message message, int timeout) throws MessageReplyTimeoutException {
        message.getHeader().setSource(getAddress());

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



}
