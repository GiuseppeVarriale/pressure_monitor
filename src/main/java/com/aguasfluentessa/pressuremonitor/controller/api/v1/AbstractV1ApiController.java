package com.aguasfluentessa.pressuremonitor.controller.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aguasfluentessa.pressuremonitor.controller.api.AbstractApiController;

@RestController
@RequestMapping("/v1")
public abstract class AbstractV1ApiController extends AbstractApiController {
    
}
