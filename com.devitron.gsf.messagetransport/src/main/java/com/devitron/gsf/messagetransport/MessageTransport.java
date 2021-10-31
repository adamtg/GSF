package com.devitron.gsf.messagetransport;

import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportInitException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportReceiveTimeoutException;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportTimeoutException;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

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
    public void init(String hostname, int port, String username, String password) throws  MessageTransportInitException;

    /**
     * Set queue name to name.  This is the name that is
     * retrieved from register(Address address).
     *
     * @param name name of the queue
     * @return boolean value if setup was successful
     */
    public boolean setupQueue(String queueName) throws MessageTransportIOException;

    /**
     * Setup default queue to send messages
     * @param queueName name of queue
     */
    public void setSendQueueDefault(String queueName);

    /**
     * Setup default queue to receive messages
     * @param queueName name of queue
     */
    public void setReceiveQueueDefault(String queueName);



    /**
     * Sends message to default queue
     *
     * @param message String representation of a message
     * @return if send was successful or not
     */
    public boolean send(String message) throws MessageTransportIOException;

    /**
     *  Sends mesasge to the queue queueName
     *
     * @param message String representation of a message
     * @param queueName queue name
     * @return
     * @throws MessageTransportIOException
     */
    public boolean send(String message, String queueName) throws MessageTransportIOException;


    /**
     * Gets message off of queue.  Blocks until a message is available.
     *
     * @return message in string form
     */
    //  public String receive() throws MessageTransportIOException;


    /**
     * Gets message off of queue.  Blocks until a message is available.
     *
     * @return message in string form
     */
    public void receive(Consumer<String>  callback) throws MessageTransportIOException;



        /**
         * Gets message of off queue.  Blocks until a message is available or
         * for timeout milliseconds.
         *
         * @param timeout timeout in milliseconds
         * @return message in string form
         */
   // public String receive(int timeout) throws MessageTransportReceiveTimeoutException, MessageTransportIOException;

    /**
     * Shuts down queue
     */
    public void close() throws MessageTransportIOException, MessageTransportTimeoutException;

}
