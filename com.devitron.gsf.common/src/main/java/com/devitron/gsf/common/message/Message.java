package com.devitron.gsf.common.message;

import java.util.UUID;

public class Message {

    private Address source;
    private Address destination;

    private String function;

    private String uuid;
    private String comment;

    public Message(Address source, Address destination) {
        this.source = source;
        this.destination = destination;
        this.uuid = generateUUID();
    }


    public Message()  {
        this.uuid = generateUUID();
    }


    String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }


    /**
     * Returns message as a raw string.
     *
     * @return message in a string format
     */
    public String toString() {

        return null;
    }

    /**
     * Returns a message as a pretty printed string
     *
     * @return message in a pretty print string format
     */
    public String toPrettyString() {
        return null;
    }


    public Address getSource() {
        return source;
    }

    public void setSource(Address source) {
        this.source = source;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
