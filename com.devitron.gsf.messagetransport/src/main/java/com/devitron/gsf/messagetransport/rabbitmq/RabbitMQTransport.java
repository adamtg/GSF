package com.devitron.gsf.messagetransport.rabbitmq;

import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportReceiveTimeoutException;


import com.devitron.gsf.messagetransport.exceptions.MessageTransportTimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class RabbitMQTransport implements MessageTransport {

    private String queueName;
    private String regQueueName;

    private String hostname;
    private int port;

    private String username;
    private String password;


    private Connection connection;
    private Channel channel;

    @Override
    public void init(String hostname, int port, String username, String password) throws  MessageTransportIOException, MessageTransportTimeoutException {
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password = password;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostname);
        factory.setPort(port);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();

        } catch (IOException e) {
            throw new MessageTransportIOException(e);
        } catch  (TimeoutException e) {
            throw new MessageTransportTimeoutException(e);
        }

    }

    @Override
    public boolean setupQueue(String name) {
        return false;
    }

    @Override
    public boolean send(String message, String queueName) {
        return false;
    }

    @Override
    public boolean send(String message) {
        return false;
    }

    @Override
    public String receive() {
        return null;
    }

    @Override
    public String receive(int timeout) throws MessageTransportReceiveTimeoutException {
        return null;
    }

    @Override
    public void close() {

    }


}
