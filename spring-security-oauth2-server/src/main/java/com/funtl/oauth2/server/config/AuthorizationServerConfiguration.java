//package com.funtl.oauth2.server.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.approval.ApprovalStore;
//import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author zhan
// * @since 2019-05-30 17:12
// */
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Qualifier("dataSource")
//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private TokenEnhancer jwtTokenEnhancer;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;
//
//
//    @Bean
//    public JdbcClientDetailsService clientDetailsService() {
//        return new JdbcClientDetailsService(dataSource);
//    }
//
//    @Bean
//    public ApprovalStore approvalStore() {
//        return new JdbcApprovalStore(dataSource);
//    }
//
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        return new JdbcAuthorizationCodeServices(dataSource);
//    }
//
//    /**
//     * 配置数据源
//     * @return
//     */
////    @Primary
////    @Bean
////    @ConfigurationProperties(prefix = "spring.datasource")
////    public DataSource dataSource(){
////        return DataSourceBuilder.create().build();
////    }
//
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        super.configure(security);
//                //允许/oauth/token调用
//        security.tokenKeyAccess("permitAll()")
//                //允许/oauth/check_token被调用
//                .checkTokenAccess("isAuthenticated()");
//    }
//
//    @Bean
//    public ClientDetailsService jdbcClientDetailes(){
//        return  new JdbcClientDetailsService(dataSource);
//    }
//
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
////        clients.inMemory()
////                .withClient("client")
////                .secret(passwordEncoder.encode("secret"))
////                .authorizedGrantTypes("authorization_code")
////                .scopes("app")
////                .redirectUris("https://www.baidu.com");
//        clients.withClientDetails(jdbcClientDetailes());
//    }
//
//    /**
//     * 令牌访问端点
//     * @param endpoints
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
////        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET)
////                .authorizationCodeServices(authorizationCodeServices);
//
//        endpoints.tokenStore(tokenStore)
//                .authorizationCodeServices(authorizationCodeServices())     //配置将授权码存放在oauth_code变中，默认存在内存中
//                .approvalStore(approvalStore())                             //配置审批存储oauth_approvals，存储用户审批过程，在一个月时间内不用再次审批
//                .authenticationManager(authenticationManager)
//                .userDetailsService(userDetailsService)
//                .reuseRefreshTokens(true);   //支持刷新令牌
//        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
//            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
//            List<TokenEnhancer> enhancers = new ArrayList<>();
//            enhancers.add(jwtAccessTokenConverter);
//            enhancers.add(jwtTokenEnhancer);
//            //将自定义Enhancer加入EnhancerChain的delegates数组中
//            enhancerChain.setTokenEnhancers(enhancers);
//            //为什么不直接把jwtTokenEnhancer加在这个位置呢？
//            endpoints.tokenEnhancer(enhancerChain)
//                    .accessTokenConverter(jwtAccessTokenConverter);
//        }
//
////        DefaultTokenServices tokenServices = new DefaultTokenServices();
////        tokenServices.setTokenStore(tokenStore());
////        tokenServices.setAuthenticationManager(authenticationManager);
////        tokenServices.setReuseRefreshToken(true);
////        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
////        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
////        tokenServices.setReuseRefreshToken(false);      //设置refresh_token是否可以重用
////
////
////        endpoints.tokenServices(tokenServices)
////                .userDetailsService(userDetailsService)
////                .accessTokenConverter(jwtAccessTokenConverter());
//
//    }
//}
