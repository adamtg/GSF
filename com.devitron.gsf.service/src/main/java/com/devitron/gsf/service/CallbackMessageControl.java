package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

import java.util.HashMap;
import java.util.function.Consumer;

public class CallbackMessageControl {

    static private CallbackMessageControl cmc = new CallbackMessageControl();
    private HashMap<String,CallbackNode> cmcMap = new HashMap<>();

    private CallbackMessageControl() { }

    static public CallbackMessageControl getCallbackMessageControl() {
        return cmc;
    }


    public void add(Message message, Consumer<Message> callback, Class messageClass) {
        cmcMap.put(message.getHeader().getUuid(), new CallbackNode(callback, messageClass));
    }

    public void run(Message message, String json) {

        String uuid = message.getHeader().getUuid();

        if (cmcMap.containsKey(uuid)) {
            CallbackNode cn = cmcMap.get(uuid);
            Consumer<Message> f = cn.callback;
            try {
                Message m = (Message)Json.jsonToObject(json, cn.messageClass);
                f.accept(m);
            } catch (UtilitiesJsonParseException e) {
                e.printStackTrace();
            } finally {
                cmcMap.remove(uuid);
            }


        }


    }

     private class CallbackNode {

         CallbackNode(Consumer<Message> callback, Class messageClass) {
             this.callback = callback;
             this.messageClass = messageClass;
         }

        Consumer<Message> callback;
        Class messageClass;
    }

}
