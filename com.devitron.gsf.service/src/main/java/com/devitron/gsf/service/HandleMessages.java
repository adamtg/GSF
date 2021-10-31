package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.messagetransport.MessageTransport;
import com.devitron.gsf.messagetransport.MessageTransportFactory;
import com.devitron.gsf.messagetransport.exceptions.MessageTransportIOException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandleMessages {

    Service service;

    public HandleMessages(Service service) {
        this.service = service;
    }

    void mainLoop() {
        boolean continueLoop = true;
        MessageTransport mt = MessageTransportFactory.getMessageTransport();

            try {
                mt.receive((json) -> {
                            Message message = null;
                            try {
                                message = (Message) Json.jsonToObject(json, Message.class);
                            } catch (UtilitiesJsonParseException e) {
                                e.printStackTrace();
                            }

                            if (message.getHeader().getType() == Message.REQUEST) {
                                handleRequest(message, json);
                            } else if (message.getHeader().getType() == Message.REPLY) {
                                handleReply(message, json);
                            }
                        }

                        );
            } catch (MessageTransportIOException e) {
                e.printStackTrace();
                continueLoop = false;
            }


    }

    private void handleRequest(Message message, String json) {

        FunctionMethodControl fmc = FunctionMethodControl.getFunctionMethodControl();

        Method method = fmc.getMethod(message.getHeader().getFunction());

        try {
            Class returnType = method.getReturnType();
            Class firstArg = method.getParameterTypes()[0];

            Message request = (Message)Json.jsonToObject(json, firstArg);
            Message reply = (Message)method.invoke(request);

            service.send(reply);

        } catch (IllegalAccessException | InvocationTargetException | UtilitiesJsonParseException | MessageTransportIOException e) {
            e.printStackTrace();
        }
    }

    private void handleReply(Message message, String json) {

        if (message.getHeader().isCallback()) {
            CallbackMessageControl cmc = CallbackMessageControl.getCallbackMessageControl();
            cmc.run(message, json);
        } else if (message.getHeader().isSync()) {
            SynchronousMessageControl smc = SynchronousMessageControl.getSynchronousMessageControl();
            smc.unlockMessage(message, json);
        }

    }

}
