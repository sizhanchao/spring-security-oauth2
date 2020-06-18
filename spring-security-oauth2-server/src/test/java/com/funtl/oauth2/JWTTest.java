package com.funtl.oauth2;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhan
 * @since 2019-12-12 17:44
 */
//@SpringBootTest
@RunWith(SpringRunner.class)
public class JWTTest {

    @Test
    public void test() {
//keytool -genkeypair -alias zhan -keyalg RSA -keypass 123456 -keystore zhan.keystore -storepass 123456
        //证书文件
        String key_location = "zhan.keystore";
        //密钥库密码
        String keyStore_password = "123456";

        //访问证书路径
        ClassPathResource resource = new ClassPathResource("zhan.keystore");
        //密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, keyStore_password.toCharArray());
        //密钥的密码，此密码和别名要匹配
        String keypassword = "123456";
        //密钥别名
        String alias = "zhan";
        //密钥对（密钥和公钥）
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keyStore_password.toCharArray());
        //私钥
        RSAPrivateKey aPrivate = (RSAPrivateKey) keyPair.getPrivate();
        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", 10001L);
        tokenMap.put("name", "司展超");
        tokenMap.put("age", 28);
        tokenMap.put("roles", "p1,p2");
        //生成jwt令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(aPrivate));

        //取出jwt令牌
        String token = jwt.getEncoded();
        System.out.println(token);
    }
    @Test
    public void testVerify() {
        //jwt令牌
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJyb2xlcyI6InAxLHAyIiwibmFtZSI6IuWPuOWxlei2hSIsImlkIjoxMDAwMSwiYWdlIjoyOH0" +
                ".fdItsVuFoYIYSdzdLySukdRusT0bOr_MFbKUpyaDnHDFGw5HVNbTv3gixPmipnX1SwFGPXVeUeoy5g33Q9ORrXRepcqHYmKNcAKDeFppe1U_lMc8k1h20RUaYVB7CihWENmoCYwDwjRD2vgw3zqSQ0CP7nUkcoMQOvDEwdPPCTk0JlZHywdblZVUQ4GYB-3AVKf-FijeFMDxWJcBhB9CAMYt6mdIgghFNT4qkmrgASi-0YZH97q8h8yyF_8JlUJbWjMmLbi3pKQdhgC434pAWuYWPrhRKZm7jX9EHC2tzHifLeS7sI108zGMQ0ukozlPxSvroNXjeo2-ZVPVbEl1AQ";
        ////公钥
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgN0Ka7Xv+2xgoUtuHtqBnKljMyBe5YZ/Zo7Jo1H9P0AwVDzPbmrBfq2Y2oqdFlcAUBs0UwKJ0FuqP6IgoRqCTBb5NmQo9nhgC0FGLF26wiLID1P0+lXoX02mhj6yqAGZDo3tMgk0xJ9pRybnqQOWJzAkISfI71by/IpOm5BZzzTNGH7sW8yxdw8K8+tFquKLMbKQcAAUa9/9l5VvIyvUci63Xt5URCWb6IDtwCNhu+cCs3ZBX6hcrdQW0VP46nG14+6fm50FpVEnQAXowfagP/ipdJcA/54sJeJ/m2vHQEbS4lKHhDUrfgIbJBaUmtk5ZkufVRkMSryjuIO1IasLbwIDAQAB-----END PUBLIC KEY-----";

        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        //获取jwt原始内容
        String claims = jwt.getClaims();
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }
}
