package com.devitron.gsf.common.configuration.exceptions;

public class ConfigFileNotFoundException extends Exception {

    public ConfigFileNotFoundException(Exception e) {
        super(e);
    }


    public ConfigFileNotFoundException(String m) {
        super(m);
    }

}
