package com.devitron.gsf.messagetransport.rabbitmq;

import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportInitException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportReceiveTimeoutException;


import com.devitron.gsf.messagetransport.exceptions.MessageTransportTimeoutException;
import com.rabbitmq.client.*;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;


public class RabbitMQTransport implements MessageTransport {

    private String sendQueueName;
    private String receiveQueueName;

    private String hostname;
    private int port;

    private String username;
    private String password;


    private Connection connection;
    private Channel channel;

    @Override
    public void init(String hostname, int port, String username, String password) throws MessageTransportInitException {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostname);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            throw new MessageTransportInitException(e);
        } catch  (TimeoutException e) {
            throw new MessageTransportInitException(e);
        }

    }

    @Override
    public boolean setupQueue(String queueName) throws MessageTransportIOException {


        try {
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            throw new MessageTransportIOException(e);
        }

        return true;
    }

    @Override
    public void setSendQueueDefault(String queueName) {
        this.sendQueueName = queueName;
    }

    @Override
    public void setReceiveQueueDefault(String queueName) {
        this.receiveQueueName = queueName;
    }



    @Override
    public boolean send(String message) throws MessageTransportIOException {

        try {
            channel.basicPublish("", sendQueueName, null, message.getBytes());
        } catch (IOException e) {
            throw new MessageTransportIOException(e);
        }

        return true;
    }

    @Override
    public boolean send(String message, String queueName) throws MessageTransportIOException {
        try {
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            throw new MessageTransportIOException(e);
        }
        return true;
    }

    @Override
    public void receive(Consumer<String> callback) throws MessageTransportIOException {


        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String m = new String(message.getBody());
            callback.accept(m);
        };




        String reply = null;
        try {
            channel.basicConsume(receiveQueueName, true, deliverCallback, consumerTag -> {});
        } catch (IOException e) {
            throw new MessageTransportIOException(e);
        }
    }

    @Override
    public void close() throws MessageTransportIOException, MessageTransportTimeoutException {

        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            throw new MessageTransportIOException(e);
        } catch (TimeoutException e) {
            throw new MessageTransportTimeoutException(e);
        }


    }


}
