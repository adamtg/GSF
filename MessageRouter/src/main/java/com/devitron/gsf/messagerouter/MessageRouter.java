package com.devitron.gsf.messagerouter;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.common.message.Message;

public class MessageRouter {


    /**
     * Takes a message, parses the header and send it
     * to the appropriate registered service
     *
     * @param receive message to route to the proper service
     */
    public void messageHandler(Message receive) {

    }


    /**
     * Given a service address, it will register the service within
     * MessageRouter and create a unique queue name.
     *
     * @param address Address of the service
     * @return unique queue name
     */
    public String registerService(Address address) {
        return null;
    }

    /**
     *  Main loop
     */
    public void mainLoop() {

    }


}
