package com.devitron.gsf.messagerouter.register;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.messagerouter.exception.AddressAlreadyRegisteredException;
import com.devitron.gsf.messagerouter.exception.AddressNotRegisteredException;

import java.util.HashMap;

public class RegisteredServices {

    HashMap<Address, String> registered = new HashMap<>();
    HashMap<String, String> maxServiceVersion = new HashMap<>();

    String addService(Address address) throws AddressAlreadyRegisteredException {

        if (registered.containsKey(address)) {
            throw new AddressAlreadyRegisteredException();
        }



        String qAddress = address.getName() + "_" + address.getVersion();
        registered.put(address, qAddress);

        return qAddress;
    }

    public void deleteService(Address address) throws AddressNotRegisteredException {

        if (!registered.containsKey(address)) {
            throw new AddressNotRegisteredException();
        }

        registered.remove(address);

    }

    public void getQueueAddress(Address address)  throws AddressNotRegisteredException {

        if (!registered.containsKey(address)) {
            throw new AddressNotRegisteredException();
        }


    }

}
