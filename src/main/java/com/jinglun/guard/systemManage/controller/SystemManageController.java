package com.jinglun.guard.systemManage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/systemManage")
public class SystemManageController {

    @RequestMapping("toSystemManageNav")
    public String toSystemManageNav() {
        return "systemManage/systemManageNav";
    }
}
