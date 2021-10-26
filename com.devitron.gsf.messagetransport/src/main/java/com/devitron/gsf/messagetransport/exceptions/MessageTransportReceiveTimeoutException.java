package com.devitron.gsf.messagetransport.exceptions;

public class MessageTransportReceiveTimeoutException extends Exception {

    public MessageTransportReceiveTimeoutException() {
        super("Receive timedout");
    }

}
