package com.funtl.oauth2.server.config;

import com.funtl.oauth2.jwt.MyJwtTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * @author zhan
 * @since 2019-12-13 15:28
 */
@Configuration
public class JwtTokenConfig {

    private static final String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgN0Ka7Xv+2xgoUtuHtqBnKljMyBe5YZ/Zo7Jo1H9P0AwVDzPbmrBfq2Y2oqdFlcAUBs0UwKJ0FuqP6IgoRqCTBb5NmQo9nhgC0FGLF26wiLID1P0+lXoX02mhj6yqAGZDo3tMgk0xJ9pRybnqQOWJzAkISfI71by/IpOm5BZzzTNGH7sW8yxdw8K8+tFquKLMbKQcAAUa9/9l5VvIyvUci63Xt5URCWb6IDtwCNhu+cCs3ZBX6hcrdQW0VP46nG14+6fm50FpVEnQAXowfagP/ipdJcA/54sJeJ/m2vHQEbS4lKHhDUrfgIbJBaUmtk5ZkufVRkMSryjuIO1IasLbwIDAQAB-----END PUBLIC KEY-----";


    @Bean
//    @ConditionalOnBean(JwtTokenEnhancer.class)
    public TokenEnhancer jwtTokenEnhancer() {
        return new MyJwtTokenEnhancer();
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(jwtTokenStore());
        return defaultTokenServices;
    }
    /**
     * token生成处理：指定签名
     */

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("zhan.keystore"), "123456".toCharArray());
        //非对称加密
        accessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("zhan"));
//        accessTokenConverter.setVerifierKey(publicKey);
        //对称加密
//        accessTokenConverter.setSigningKey("123456");
        return accessTokenConverter;
    }
}
