package com.devitron.gsf.messagerouter.register;

import com.devitron.gsf.common.message.Address;
import com.devitron.gsf.messagerouter.exception.AddressAlreadyRegisteredException;
import com.devitron.gsf.messagerouter.exception.AddressNotRegisteredException;

import java.util.HashMap;

public class RegisteredServiceController {

    HashMap<Address, String> registered = new HashMap<>();
    HashMap<String, String> maxServiceVersion = new HashMap<>();
    static private RegisteredServiceController rsc = new RegisteredServiceController();

    private RegisteredServiceController() { }

    static public RegisteredServiceController getRegisteredServiceController() {
        return rsc;
    }


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

    public String getQueueAddress(Address address)  throws AddressNotRegisteredException {

        if (!registered.containsKey(address)) {
            throw new AddressNotRegisteredException();
        }

        return registered.get(address);
    }


    public boolean doesAddressExist(Address address) {
        return registered.containsKey(address);
    }


    public boolean doesServiceExist(String service) {
        boolean exists = false;

        for (Address address : registered.keySet()) {
            if (address.getName().equals(service)) {
                exists = true;
                break;
            }
        }

        return exists;
    }
}
