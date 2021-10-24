package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.service.exceptions.MessageReplyTimeoutException;

import java.util.function.Function;

public abstract class Service {



    abstract public Address getAddress();


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
    public Message send(Message message, int timeout) throws MessageReplyTimeoutException {
        message.getHeader().setSource(getAddress());

        return new Message();
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
