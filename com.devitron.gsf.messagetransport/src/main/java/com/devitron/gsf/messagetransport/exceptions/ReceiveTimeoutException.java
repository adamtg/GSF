package com.devitron.gsf.messagetransport.exceptions;

public class ReceiveTimeoutException extends Exception {

    public ReceiveTimeoutException() {
        super("Receive timedout");
    }

}
