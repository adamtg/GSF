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
        setSource(new Address(message.getDestination().getName(), message.getDestination().getVersion()));
        setDestination(new Address(message.getSource().getName(), message.getSource().getVersion()));
        setUuid(message.getUuid());
        setComment(message.getComment());
    }



}
