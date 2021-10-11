package com.devitron.gsf.messagerouter.exception;

public class AddressAlreadyRegisteredException extends Exception {

    public AddressAlreadyRegisteredException() {
        super("Address is already registered");
    }
}
