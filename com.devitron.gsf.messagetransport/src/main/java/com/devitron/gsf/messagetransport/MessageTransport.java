package com.devitron.gsf.messagetransport;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagetransport.exceptions.ReceiveTimeoutException;

public class MessageTransport {

    /**
     * Using address, it will send a message to Message Router
     * to get queue name
     *
     * @param address address of service
     * @return name of queue for service
     */
    public String register(Address address) {
        return null;
    }


    /**
     * Set queue name to name.  This is the name that is
     * retrieved from register(Address address).
     *
     * @param name name of the queue
     * @return boolean value if setup was successful
     */
    public boolean setupQueue(String name) {

        return true;
    }

    /**
     * Sends message
     *
     * @param message message to send
     * @return if send was successful or not
     */
    public boolean send(Message message) {

        return true;
    }

    /**
     * Sends message, message represented as a string
     *
     * @param message String representation of a message
     * @return if send was successful or not
     */
    public boolean send(String message) {

        return true;
    }


    /**
     * Gets message off of queue.  Blocks until a message is available.
     *
     * @return message in string form
     */
    public String receive() {
        return null;
    }

    /**
     * Gets message of off queue.  Blocks until a message is available or
     * for timeout milliseconds.
     *
     * @param timeout timeout in milliseconds
     * @return message in string form
     */
    public String receive(int timeout) throws ReceiveTimeoutException {
        return null;
    }

    /**
     * Shuts down queue
     */
    public void close() {

    }

}
