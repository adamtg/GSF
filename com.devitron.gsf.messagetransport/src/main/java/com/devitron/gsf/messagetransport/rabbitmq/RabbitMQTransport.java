package com.devitron.gsf.messagetransport.rabbitmq;

import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportReceiveTimeoutException;


import com.devitron.gsf.messagetransport.exceptions.MessageTransportTimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class RabbitMQTransport implements MessageTransport {

    private String queueName;

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

        queueName = name;
        try {
            channel.queueDeclare(queueName, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean send(String message) {

        try {
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String receive() {

        String reply = null;

        try {
            GetResponse gr =  channel.basicGet(queueName, true);
            reply = new String(gr.getBody());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return reply;
    }

    @Override
    public String receive(int timeout) throws MessageTransportReceiveTimeoutException {

        return receive();
    }

    @Override
    public void close() {

        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }


}
