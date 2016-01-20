package com.pronoia.karaf.service.impl;

import com.pronoia.karaf.service.TestService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestServiceImplementation implements TestService {

    public String execute() {
        return this.getClass().getSimpleName() + ".execute(): hashCode=" + this.hashCode();
    }

}
