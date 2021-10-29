package com.devitron.gsf.messagerouter.register;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.messagerouter.messages.Messages;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

public class Register {
    /**
     * Given a service address, it will register the service within
     * MessageRouter and create a unique queue name.
     *
     * @param json String on incoming message
     * @return unique queue name
     */
    static public Messages.RegisterServiceReply registerService(String json) {

        Messages.RegisterServiceRequest request = null;

        try {
            request = (Messages.RegisterServiceRequest) Json.jsonToObject(json, Messages.RegisterServiceRequest.class);
        } catch (UtilitiesJsonParseException e) {
            e.printStackTrace();
        }

        Messages.RegisterServiceReply reply = new Messages.RegisterServiceReply(request);


        RegisteredServiceController rsc = RegisteredServiceController.getRegisteredServiceController();

        // Handle dup and unique
        // boolean serviceExists = rsc.doesServiceExist(request.getHeader().getSource().getName());

        String queueName = request.getHeader().getSource().getName() + "_" +
                request.getHeader().getSource().getVersion() + "_" +
                request.randomString;

        reply.shutdown = false;
        reply.queueName = queueName;

        return reply;


    }

}
