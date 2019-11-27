package com.grq.provider.service;

import com.grq.provider.api.EchoService;
import org.apache.dubbo.config.annotation.Service;

@Service(version = "1.0.0")
public class EchoServiceImpl implements EchoService {
    @Override
    public String echoHi(String string) {
        return "Hello "+string;
    }
}
