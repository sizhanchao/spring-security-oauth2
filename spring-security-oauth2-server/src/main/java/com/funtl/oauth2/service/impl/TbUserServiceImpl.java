package com.funtl.oauth2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.funtl.oauth2.domain.TbUser;
import com.funtl.oauth2.mapper.TbUserMapper;
import com.funtl.oauth2.service.TbUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TbUserServiceImpl implements TbUserService {

    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser getByUserName(String username) {
        QueryWrapper ew = new QueryWrapper();
        ew.eq("username", username);
        TbUser tbUser = tbUserMapper.selectOne(ew);
        return tbUser;
    }

    @Override
    public TbUser getByUserById(String id) {
        return  tbUserMapper.selectById(id);
    }
}

