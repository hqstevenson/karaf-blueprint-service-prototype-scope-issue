package com.pronoia.karaf.service.impl;

import com.pronoia.karaf.service.TestService;

public class TestServiceThree implements TestService {
    @Override
    public String execute() {
        return this.getClass().getSimpleName() + ".execute(): hashCode=" + this.hashCode();
    }
}
