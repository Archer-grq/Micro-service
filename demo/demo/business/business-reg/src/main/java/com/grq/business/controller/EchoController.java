package com.grq.business.controller;

import com.grq.provider.api.EchoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {
    @Reference(version = "1.0.0")
    private EchoService echoService;

    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        return echoService.echoHi(string);
    }


}
