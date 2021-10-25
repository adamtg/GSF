package com.devitron.gsf.common.message;

import com.devitron.gsf.utilities.Utilities;

import java.util.UUID;

public class Message {

    Header header = new Header();

    public Message(Address source, Address destination) {
        this.header.setSource(source);
        this.header.setDestination(destination);
        this.header.setUuid(Utilities.generateUUID());
    }


    public Message()  {
        this.header.setUuid(Utilities.generateUUID());
    }

    public Header getHeader() {
        return header;
    }


    /**
     * Returns message as a raw JSON string.
     *
     * @return message in a string format
     */
    public String toString() {

        return null;
    }

    /**
     * Returns a message as a pretty printed JSON string
     *
     * @return message in a pretty print string format
     */
    public String toPrettyString() {
        return null;
    }



    /**
     * Given a message as a string, create a message based object
     * of type classType
     *
     * @param messageString message as a string
     * @param classType type of class to create
     * @return
     */
    public static Message createMessage(String messageString, Class classType) {

        return null;

    }


}
