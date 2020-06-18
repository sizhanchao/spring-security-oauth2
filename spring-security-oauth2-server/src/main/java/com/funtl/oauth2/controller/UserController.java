package com.funtl.oauth2.controller;

import com.funtl.oauth2.domain.TbUser;
import com.funtl.oauth2.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhan
 * @since 2019-12-13 10:06
 */
@RestController
public class UserController {

    @Autowired
    TbUserService tbUserService;

    @RequestMapping("/data")
    public TbUser testData(String id) {
        return tbUserService.getByUserById(id);
    }

    @RequestMapping(value = "/context", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('System')")
    @ResponseBody
    public Object get(){
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx;
    }
}
