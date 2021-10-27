package com.devitron.gsf.common.message;

import com.devitron.gsf.utilities.Utilities;

public class Reply extends Message {

    public Reply() {
         header.setUuid(Utilities.generateUUID());
    }

    public Reply(Address source, Address destination) {
        super(source, destination);
    }

    public Reply(Message message) {
        setFromMessage(message);
    }

    void setFromMessage(Message message) {
        header.setSource(new Address(message.getHeader().getDestination().getName(), message.getHeader().getDestination().getVersion()));
        header.setDestination(new Address(message.getHeader().getSource().getName(), message.getHeader().getSource().getVersion()));
        header.setUuid(message.getHeader().getUuid());
        header.setComment(message.getHeader().getComment());
        header.setCallback(message.getHeader().isCallback());
        header.setSync(message.getHeader().isSync());
        header.setFunction(message.getHeader().getFunction());
    }



}
