package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.service.HairStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/hairstyle")
public class HairStyleController {

    @Autowired
    private HairStyleService hairStyleService;


}
