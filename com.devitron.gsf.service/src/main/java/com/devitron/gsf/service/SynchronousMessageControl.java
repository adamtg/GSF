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


    public Message waitForMessage(Message message, long timeout, Class replyMessageClass) throws InterruptedException, ServiceMessageReplyTimeoutException {

        String Uuid = message.getHeader().getUuid();
        Message replyMessage = null;
        boolean acquiredLock = true;

        SyncNode sn = new SyncNode();
        sn.lock.lock();
        sn.replyMessageClass = replyMessageClass;

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

    public void unlockMessage(Message message, String jsonMessage) {

        try {
            message = (Message) Json.jsonToObject(jsonMessage, Message.class);

        } catch (UtilitiesJsonParseException e) {
            e.printStackTrace();
            return;
        }

        if (syncMap.containsKey(message.getHeader().getUuid())) {
            SyncNode sn = syncMap.get(message.getHeader().getUuid());

            Message m = null;
            try {
                m = (Message)Json.jsonToObject(jsonMessage, sn.replyMessageClass);
            } catch (UtilitiesJsonParseException e) {
                e.printStackTrace();
            }


            sn.message = m;
            sn.lock.unlock();
        }


    }


    private class SyncNode {
        Lock lock = new ReentrantLock();
        Message message = null;
        Class replyMessageClass;
    }


}
