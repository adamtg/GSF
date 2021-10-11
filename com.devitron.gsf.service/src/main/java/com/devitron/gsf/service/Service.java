package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.service.exceptions.MessageReplyTimeoutException;

import java.util.function.Function;

public class Service {

    /**
     * Sends out a message asynchronously.  The message reply is
     * handled by the callback.  If callback is NULL, the message
     * will be sent, and the reply will be silently tossed.
     *
     * @param message Message to be sent
     * @param callback Function to handle message reply
     */
    public void send(Message message, Function<Message, Message> callback) {

    }

    /**
     * Sends out a message synchronously. The call will block until a reply
     * is received.  If a reply message is not sent, the call will block
     * indefinitely.
     *
     * @param message Message to be sent
     * @return Reply message
     */
    public Message send(Message message) {
        return new Message();
    }

    /**
     *
     *
     * @param message Message to be sent
     * @param timeout How long to wait for reply in microseconds
     * @return Reply message
     */
    public Message send(Message message, int timeout) throws MessageReplyTimeoutException {
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
