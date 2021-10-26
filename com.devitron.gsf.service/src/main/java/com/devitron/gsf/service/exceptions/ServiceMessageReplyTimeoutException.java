package com.devitron.gsf.service.exceptions;

public class ServiceMessageReplyTimeoutException extends Exception {

    public ServiceMessageReplyTimeoutException() {
        super("Synchronous send call timeout");
    }


}
