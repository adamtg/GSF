package com.devitron.gsf.messagetransport;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagetransport.exceptions.ReceiveTimeoutException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface MessageTransport {


    /**
     * Does any type of initialization needed
     * for the message broker/system
     *
     * @param hostname (if applicable) name of the host of the message broker
     * @param port (if applicable) port the message broker is listening on
     * @param username (if applicable) username to connect to the message broker
     * @param password (if applicable) password to connect to the message broker
     */
    public void init(String hostname, int port, String username, String password)  throws IOException, TimeoutException;

    /**
     * Set queue name to name.  This is the name that is
     * retrieved from register(Address address).
     *
     * @param name name of the queue
     * @return boolean value if setup was successful
     */
    public boolean setupQueue(String name);

    /**
     * Sends message
     *
     * @param message message to send
     * @return if send was successful or not
     */
    public boolean send(Message message);


    /**
     * Sends a message to the queue queueName instead of the
     * default queue that was configured with setupQueue
     *
     * @param message message to send
     * @param queueName name of alternative queue
     * @return if send was successful or not
     */
    public boolean send(Message message, String queueName);

    /**
     * Sends message, message represented as a string
     *
     * @param message String representation of a message
     * @return if send was successful or not
     */
    public boolean send(String message);

    /**
     * Sends a message, represented by a string, to the queue
     * queueName instead of the default queue that was
     * configured with setupQueue.
     *
     * @param message String representation of a message
     * @param queueName name of alternative queue
     * @return if send was successful or not
     */
    public boolean send(String message, String queueName);


    /**
     * Gets message off of queue.  Blocks until a message is available.
     *
     * @return message in string form
     */
    public String receive();

    /**
     * Gets message of off queue.  Blocks until a message is available or
     * for timeout milliseconds.
     *
     * @param timeout timeout in milliseconds
     * @return message in string form
     */
    public String receive(int timeout) throws ReceiveTimeoutException;

    /**
     * Shuts down queue
     */
    public void close();

}
