package com.devitron.gsf.service.exceptions;

public class MessageReplyTimeoutException extends Exception {

    public MessageReplyTimeoutException() {
        super("Synchronous send call timeout");
    }


}
