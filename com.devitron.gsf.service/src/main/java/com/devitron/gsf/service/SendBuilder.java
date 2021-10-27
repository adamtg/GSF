package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;

import java.util.function.Consumer;
import java.util.function.Function;

public class SendBuilder {

    private Message message = null;
    boolean isSync = false;
    Consumer<String> callback = null;


    public SendBuilder(Message message) {
        this.message = message;
    }


    public SendBuilder withSync(boolean isSync) {
        this.isSync = isSync;
        return this;
    }

    public SendBuilder withCallback(Consumer<String> callback) {
        this.callback = callback;
        return this;
    }



}



