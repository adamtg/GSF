package com.devitron.gsf.messagetransport;

import com.devitron.gsf.messagetransport.rabbitmq.RabbitMQTransport;

public class MessageTransportFactory {

    public static MessageTransport getMessageTransport() {
        return new RabbitMQTransport();
    }


}
