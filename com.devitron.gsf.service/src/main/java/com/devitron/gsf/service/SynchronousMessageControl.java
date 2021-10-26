package com.devitron.gsf.service;

import com.devitron.gsf.common.message.Message;
import com.devitron.gsf.utilities.Json;
import com.devitron.gsf.utilities.exceptions.UtilitiesJsonParseException;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronousMessageControl {

        private HashMap<String, SyncNode> syncMap = new HashMap<>();
        private static SynchronousMessageControl smc = new SynchronousMessageControl();

        private SynchronousMessageControl() { };

        static public SynchronousMessageControl getSynchronousMessageControl() {
                return smc;
        }


        public void lockMessage(Message message) {

                String Uuid = message.getHeader().getUuid();

                SyncNode sn = new SyncNode();
                sn.lock.lock();

                syncMap.put(Uuid, sn);
        }


        public String waitForMessage(Message message) {

                String Uuid = message.getHeader().getUuid();
                String replyMessage = null;

                if (syncMap.containsKey(Uuid)) {
                        SyncNode sn = syncMap.get(Uuid);
                        sn.lock.lock();
                        replyMessage = sn.message;
                        syncMap.remove(Uuid);
                }

                return replyMessage;
        }

        public void unlockMessage(String jsonMessage) {

                String uuid = null;

                Message message = null;
                try {
                        message = (Message)Json.jsonToObject(jsonMessage, Message.class);
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
