package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.utilities.Json;

import java.util.HashMap;
import java.util.function.Consumer;

public class CallbackMessageControl {

    static private CallbackMessageControl cmc = new CallbackMessageControl();
    private HashMap<String, Consumer<String>> cmcMap = new HashMap<>();

    private CallbackMessageControl() { }

    static public CallbackMessageControl getCallbackMessageControl() {
        return cmc;
    }


    public void add(Message message, Consumer<String> callback) {
        cmcMap.put(message.getHeader().getUuid(), callback);
    }

    public void run(Message message) {

        String uuid = message.getHeader().getUuid();

        if (cmcMap.containsKey(uuid)) {
            Consumer<String> f = cmcMap.get(uuid);
            String json = Json.objectToJson(message);
            cmcMap.remove(uuid);
            f.accept(json);
        }


    }

}
