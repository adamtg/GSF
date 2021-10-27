package com.devitron.gsf.messagerouter.messages;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.common.message.Reply;
import com.devitron.gsf.common.message.Request;

public class Messages {

    public static Address destination = new Address("MessageRouter", "0.01");


    /**
     *  Register Service
     */
    public static class RegisterServiceRequest extends Request {

        public RegisterServiceRequest(Address destination) {
            super(destination);
            getHeader().setFunction("RegisterService");
        }

        public String randomString;
        public Integer shutdownOnDup;
        public Integer shutdownOnUnique;


    }

    public static class RegisterServiceReply extends Reply {

        public RegisterServiceReply(Message message) {
            super(message);
        }

        public Boolean shutdown;

    }



}
