package com.funtl.oauth2;

import com.funtl.oauth2.domain.TbUser;
import com.funtl.oauth2.mapper.TbUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhan
 * @since 2019-11-05 10:36
 */
@SpringBootTest(classes = Oauth2ServerApplication.class)
@RunWith(SpringRunner.class)
public class TestBCrypt {
    @Autowired
    TbUserMapper tbUserMapper;

    @Test
    public void test01() {
        TbUser tbUser = tbUserMapper.selectById("37");
        System.out.println(tbUser);
    }
    @Test
    public void testBCrypt() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(6);
        String encode = passwordEncoder.encode("123456");
        System.out.println("encode:"+encode);


        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encode1 = encoder.encode("secret");
        System.out.println("----------->"+encode1);


        System.out.println("================================");
        String idForEncode = "sha256";
        Map encoders = new HashMap<>();

        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());

        PasswordEncoder passwordEncoder222 =
                new DelegatingPasswordEncoder(idForEncode, encoders);
        String encode2 = passwordEncoder222.encode("123456");

        System.out.println("=============sha256"+encode2);

        String hashpw = BCrypt.hashpw("secret", BCrypt.gensalt());
        System.out.println(hashpw);
    }

    @Test
    public void testFor() {
        for (; ; ) {
            System.out.println("1234563");
        }
    }

    @Test
    public void testReentrantLock() {
        Runnable t1=new MyThread();
        new Thread(t1,"t1").start();
        new Thread(t1,"t2").start();
        new Thread(t1,"t3").start();
        new Thread(t1,"t4").start();
    }
}
class MyThread implements Runnable {

    private Lock lock=new ReentrantLock();
    public void run() {
        lock.lock();
        try{
            for(int i=0;i<5;i++)
                System.out.println(Thread.currentThread().getName()+":"+i);
        }finally{
            lock.unlock();
        }
    }

}
