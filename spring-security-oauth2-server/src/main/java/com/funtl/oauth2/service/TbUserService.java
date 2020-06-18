package com.funtl.oauth2.service;

import com.funtl.oauth2.domain.TbUser;

public interface TbUserService {

    TbUser getByUserName(String username);

    TbUser getByUserById(String id);
}

