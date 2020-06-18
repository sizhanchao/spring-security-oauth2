package com.funtl.oauth2.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class Oauth2Config {

    /**
     * 认证服务器
     */
    @Configuration
    @EnableAuthorizationServer
    public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        //注入自定义实现用户信息服务，网上大多是基于内存的操作，次实现表示从数据库中读取用户信息
        @Autowired
        private UserDetailsService userDetailsService;

        //数据源
        @Qualifier("dataSource")
        @Autowired
        private DataSource dataSource;

        //认证管理器authenticationManager 开启密码授权
        @Autowired
        private AuthenticationManager authenticationManager;

        //jwtToken增强，自定义实现token扩展
        @Autowired
        private TokenEnhancer jwtTokenEnhancer;

        //token存储，自定义实现保存到数据库中
        @Autowired
        private TokenStore tokenStore;

        //token转换器
        @Autowired
        private JwtAccessTokenConverter jwtAccessTokenConverter;

        //客户端信息，来源与DB
        @Bean
        public JdbcClientDetailsService clientDetailsService() {
            return new JdbcClientDetailsService(dataSource);
        }

        //自定义将自定授权保存数据库
        @Bean
        public ApprovalStore approvalStore() {
            return new JdbcApprovalStore(dataSource);
        }

        //将授权码保存数据库
        @Bean
        public AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        //授权服务安全配置，单体应用注意配置checkTokenAccess放行。
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            super.configure(security);
            //允许/oauth/token调用
            security.tokenKeyAccess("permitAll()")
                    //允许/oauth/check_token被调用
//                    .checkTokenAccess("permitAll()");
                    .checkTokenAccess("isAuthenticated()");
        }

        //配置客户但详情从数据库读取，默认手动添加到oauth2客户端表中的数据
        @Bean
        public ClientDetailsService jdbcClientDetailes() {
            return new JdbcClientDetailsService(dataSource);
        }

        //客户但详情配置，基于JDBC
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

//        clients.inMemory()
//                .withClient("client")
//                .secret(passwordEncoder.encode("secret"))
//                .authorizedGrantTypes("authorization_code")
//                .scopes("app")
//                .redirectUris("https://www.baidu.com");
            clients.withClientDetails(jdbcClientDetailes());
        }

        /**
         * 令牌访问端点
         *
         * @param endpoints
         * @throws Exception
         */
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET)
//                .authorizationCodeServices(authorizationCodeServices);

            endpoints.tokenStore(tokenStore)
                    .authorizationCodeServices(authorizationCodeServices())     //配置将授权码存放在oauth_code变中，默认存在内存中
                    .approvalStore(approvalStore())                             //配置审批存储oauth_approvals，存储用户审批过程，在一个月时间内不用再次审批
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService)
                    .reuseRefreshTokens(true);   //支持刷新令牌
            if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
                TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
                List<TokenEnhancer> enhancers = new ArrayList<>();
                enhancers.add(jwtAccessTokenConverter);
                enhancers.add(jwtTokenEnhancer);
                //将自定义Enhancer加入EnhancerChain的delegates数组中
                enhancerChain.setTokenEnhancers(enhancers);
                //为什么不直接把jwtTokenEnhancer加在这个位置呢？
                endpoints.tokenEnhancer(enhancerChain)
                        .accessTokenConverter(jwtAccessTokenConverter);
            }
        }
    }
    /**
     * 资源服务器
     */
    @EnableResourceServer
    public class ResourcesServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private DefaultTokenServices tokenServices;
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            //资源id可再次手动设置任意字符串，如果不设置，则需要在数据oauth_client_details中的resource_ids填写固定值"oauth2-resource"
            resources.resourceId("res1");
            resources.tokenServices(tokenServices);
            super.configure(resources);
        }

        //配置需要拦截的资源，这里可扩展的比较多，自由发挥
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/context")
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated();
        }
    }
}
