package com.devitron.gsf.messagetransport.rabbitmq;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagerouter.messages.Messages;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.exceptions.ReceiveTimeoutException;



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
    public void init(String hostname, int port, String username, String password) throws IOException, TimeoutException {
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

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String register(Address address) throws IOException {

        // create and send the request
        Messages.RegisterServiceRequest request = new Messages.RegisterServiceRequest(address);
        Channel regChannel = connection.createChannel()) {
        channel.queueDeclare(regQueueName, false, false, false, null);
        channel.basicPublish("", regQueueName, null, request.toString().getBytes());

        


            return null;
    }

    @Override
    public boolean setupQueue(String name) {
        return false;
    }

    @Override
    public boolean send(Message message) {
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
    public String receive(int timeout) throws ReceiveTimeoutException {
        return null;
    }

    @Override
    public void close() {

    }


}
