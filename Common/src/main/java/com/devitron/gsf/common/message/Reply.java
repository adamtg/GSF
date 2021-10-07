package com.devitron.gsf.common.message;

public class Reply extends Message {

    public Reply() {
        setUuid(generateUUID());
    }

    public Reply(Address source, Address destination) {
        super(source, destination);
    }

    public Reply(Message message) {
        setFromMessage(message);
    }

    void setFromMessage(Message message) {
        setSource(message.getDestination());
        setDestination(message.getSource());
        setUuid(message.getUuid());
        setComment(message.getComment());
    }



}
