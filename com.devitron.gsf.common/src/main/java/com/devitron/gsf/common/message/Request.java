package com.devitron.gsf.common.message;

public class Request extends Message {

    public Request(Address source, Address destination) {
        super(source, destination);
    }

    public Request(Address destination) {
        super(destination);
    }


    public Request()  {
        super();
    }

    public void setFunction(String function) {
        getHeader().setFunction(function);
    }

    public String getFunction() {
        return getHeader().getFunction();
    }


}
