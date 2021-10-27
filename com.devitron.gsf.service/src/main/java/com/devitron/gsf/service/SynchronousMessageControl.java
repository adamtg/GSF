package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.service.exceptions.ServiceMessageReplyTimeoutException;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronousMessageControl {

    private HashMap<String, SyncNode> syncMap = new HashMap<>();
    private static SynchronousMessageControl smc = new SynchronousMessageControl();

    private SynchronousMessageControl() {
    }

    ;

    static public SynchronousMessageControl getSynchronousMessageControl() {
        return smc;
    }


    public String waitForMessage(Message message, long timeout) throws InterruptedException, ServiceMessageReplyTimeoutException {

        String Uuid = message.getHeader().getUuid();
        String replyMessage = null;
        boolean acquiredLock = true;

        SyncNode sn = new SyncNode();
        sn.lock.lock();

        syncMap.put(Uuid, sn);


        if (timeout == -1) {
            sn.lock.lock();
        } else {
            acquiredLock = sn.lock.tryLock(timeout, TimeUnit.MILLISECONDS);
        }

        if (!acquiredLock) {
            syncMap.remove(Uuid);
            throw new ServiceMessageReplyTimeoutException();
        }

        replyMessage = sn.message;
        syncMap.remove(Uuid);

        return replyMessage;
    }

    public void unlockMessage(String jsonMessage) {

        String uuid = null;

        Message message = null;
        try {
            message = (Message) Json.jsonToObject(jsonMessage, Message.class);
            uuid = message.getHeader().getUuid();

        } catch (UtilitiesJsonParseException e) {
            e.printStackTrace();
            return;
        }

        if (syncMap.containsKey(uuid)) {
            SyncNode sn = syncMap.get(uuid);
            sn.message = jsonMessage;
            sn.lock.unlock();
        }


    }


    private class SyncNode {
        Lock lock = new ReentrantLock();
        String message = null;
    }


}
