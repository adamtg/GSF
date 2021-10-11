package com.devitron.gsf.common.message;

import java.util.Objects;

public class Address {
    private String name;
    private String version;

    public Address() { };
    public Address(String name, String version) {
        this.name = name;
        this.version = version;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return getName().equals(address.getName()) && getVersion().equals(address.getVersion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getVersion());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
