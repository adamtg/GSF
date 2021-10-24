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
     * Using address, it will send a message to Message Router
     * to get queue name
     *
     * @param address address of service
     * @return name of queue for service
     */
    public String register(Address address) throws IOException;

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
     * Sends message, message represented as a string
     *
     * @param message String representation of a message
     * @return if send was successful or not
     */
    public boolean send(String message);


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
