package com.devitron.gsf.service;

import com.devitron.gsf.common.configuration.Configuration;
import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.MessageTransportFactory;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportInitException;
import com.devitron.gsf.service.annotations.RPCMethod;
import com.devitron.gsf.service.exceptions.ServiceMessageReplyTimeoutException;
import com.devitron.gsf.service.exceptions.ServiceRegistrationFailureException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.Utilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Consumer;

public abstract class Service {

    Configuration config;
    MessageTransport mt = null;


    abstract public Address getAddress();

    abstract public String getServiceName();


    /**
     * Sets up the connection to the message broker, including
     * registering with the MessageRouter
     *
     * @return if service should shutdown
     * @throws ServiceRegistrationFailureException
     * @throws MessageTransportInitException
     */
    public boolean setupMessaging() throws ServiceRegistrationFailureException, MessageTransportInitException {

        mt = MessageTransportFactory.getMessageTransport();
        mt.init(config.getGlobal().getMessageBrokerAddress(), config.getGlobal().getMessageBrokerPort(),
                config.getGlobal().getMessageBrokerUsername(), config.getGlobal().getMessageBrokerPassword());

        boolean shutdown = false;

        shutdown = register();


        if (shutdown) {
            shutdownMessageBrokerConnection();
        }

        return shutdown;
    }

    /**
     * Registers service with MessageRouter
     *
     * @return if service should shutdown
     * @throws ServiceRegistrationFailureException
     */
    private boolean register() throws ServiceRegistrationFailureException {

        boolean shutdown = false;
        String receiveQueueName;
        String sendQueueName;

        try {
            com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceRequest regRequest =
                    new com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceRequest(getAddress());

            regRequest.shutdownOnDup = config.getGlobal().getShutdownOnDup();
            regRequest.shutdownOnUnique = config.getGlobal().getShutdownOnUnique();
            regRequest.randomString = Utilities.generateRandomString(5);

            Address serviceAddress = getAddress();
            receiveQueueName = serviceAddress.getName() + "_" + serviceAddress.getVersion() + "_" + regRequest.randomString;

            mt.setupQueue(receiveQueueName);
            mt.setReceiveQueueDefault(receiveQueueName);

            mt.setSendQueueDefault(config.getGlobal().getSendMessageQueue());

            com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply regReply =
                    (com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply)send(regRequest, -1, com.devitron.gsf.messagerouter.messages.Messages.RegisterServiceReply.class) ;

            shutdown = regReply.shutdown;
        } catch (Exception e) {
            throw new ServiceRegistrationFailureException(e);
        }

        return shutdown;
    }


    void startMainLoop() {
        HandleMessages hm = new HandleMessages(this);

        hm.mainLoop();
    }

    /**
     * Sends out a message asynchronously.  The message reply is
     * handled by the callback.  If callback is NULL, the message
     * will be sent, and the reply will be silently tossed.
     *
     * @param message  Message to be sent
     * @param callback Function to handle message reply
     * @param replyClass the class type of the reply message
     * @throws MessageTransportIOException
     */
    public void send(Message message, Consumer<Message> callback, Class replyClass) throws MessageTransportIOException {
        message.getHeader().setCallback(true);

        CallbackMessageControl cmc = CallbackMessageControl.getCallbackMessageControl();
        cmc.add(message, callback, replyClass);

        send(message);
    }

    /**
     * Sends out a message asynchronously.
     *
     * @param message Message to be sent
     * @throws MessageTransportIOException
     */
    public boolean send(Message message) throws MessageTransportIOException {
        message.getHeader().setSource(getAddress());
        String json = Json.objectToJson(message);

        boolean success = mt.send(json);
        if (!success) {
            // log a critical error
        }

        return success;
    }

    /**
     * Sends out a message synchronously.  If a reply
     * is net received with timeout microseconds, an
     * exception is thrown.
     * <p>
     * A timeout of -1 means to wait indefinitely for
     * the reply
     *
     * @param message Message to be sent
     * @param timeout How long to wait for reply in microseconds
     * @param replyClass class of the class type of the reply message
     * @return Reply message
     * @throws ServiceMessageReplyTimeoutException
     * @throws MessageTransportIOException
     * @throws InterruptedException
     */
    public Message send(Message message, int timeout, Class replyClass) throws ServiceMessageReplyTimeoutException, MessageTransportIOException, InterruptedException {
        message.getHeader().setSync(true);


        boolean success = send(message);
        if (success) {
            SynchronousMessageControl smt = SynchronousMessageControl.getSynchronousMessageControl();
            smt.waitForMessage(message, -1, replyClass);
        }

        return null;
    }

    /**
     * Removes queue and shutdown message broker connection
     */
    public void shutdownMessageBrokerConnection() {

        // remove messaging queue
        // close connection to messaging queue

    }

    /**
     * Given a class name, it will add in all the methods
     * that are annotated with the RPCMethod annotation
     *
     * @param className class with methods that have the RPCMethod annotations
     */
    public void mapFunctionsToMethods(Class className) {

        Method[] methods = className.getMethods();
        String functionName = null;
        FunctionMethodControl fmc = FunctionMethodControl.getFunctionMethodControl();

        for (Method method : methods) {

            Annotation[] annotations = method.getAnnotations();

            for (Annotation a : annotations) {
                if (a instanceof RPCMethod) {
                    RPCMethod rm = (RPCMethod) a;
                    if (rm.functionName().isEmpty()) {
                        functionName = method.getName();
                    } else {
                        functionName = rm.functionName();
                    }
                    fmc.putMethod(functionName, method);
                }

            }

        }
    }

}
