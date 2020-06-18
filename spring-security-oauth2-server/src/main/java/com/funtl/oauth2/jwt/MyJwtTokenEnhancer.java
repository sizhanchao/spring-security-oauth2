package com.funtl.oauth2.jwt;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * token 增强器        Jwt token 扩展
 *
 * @author zhan
 * @since 2019-12-13 14:57
 */
public class MyJwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication().getName();
        // 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
        User user = (User) authentication.getUserAuthentication().getPrincipal();
        /** 自定义一些token属性 ***/
        final Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("userName", userName);
        additionalInformation.put("address", "陕西省西安市");
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//        OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
            return accessToken;







//        Map<String, Object> info = new HashMap<>(2);
//        info.put("username", "莹莹");
//        info.put("age", 26);
//        info.put("address", "陕西省西安市");
//        //设置附加信息
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
//        System.out.println(accessToken.getAdditionalInformation());
//        return accessToken;
    }
}
