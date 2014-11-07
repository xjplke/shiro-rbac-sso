package me.itsoho.rbac.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import me.itsoho.shiro.UserRealm;
import me.itsoho.shiro.cluster.JedisManager;
import me.itsoho.shiro.cluster.listener.CustomSessionListener;
import me.itsoho.shiro.cluster.session.CustomShiroSessionDAO;
import me.itsoho.shiro.cluster.session.JedisShiroSessionRepository;
import me.itsoho.shiro.cluster.session.ShiroSessionRepository;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.ExecutorServiceSessionValidationScheduler;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class WebSecurityConfig  {
	
	@Bean
	@Autowired
	public DefaultWebSecurityManager securityManager(DataSource dataSource,UserRealm userRealm,JedisManager jedisManager){
		
		
		JedisShiroSessionRepository shiroSessionRepository = new JedisShiroSessionRepository();
		shiroSessionRepository.setJedisManager(jedisManager);
		
		CustomShiroSessionDAO customShiroSessionDAO = new CustomShiroSessionDAO();
		customShiroSessionDAO.setShiroSessionRepository(shiroSessionRepository);
		//customShiroSessionDAO.setSessionIdGenerator(sessionIdGenerator);
		
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setGlobalSessionTimeout(1200000);
		defaultWebSessionManager.setSessionDAO(customShiroSessionDAO);
		defaultWebSessionManager.setSessionIdCookie(new SimpleCookie("JSESSIONID_COOKIE"));
		defaultWebSessionManager.getSessionIdCookie().setHttpOnly(true);
		
		List<SessionListener> listeners = new ArrayList<SessionListener>();
		CustomSessionListener c = new CustomSessionListener();
		c.setShiroSessionRepository(shiroSessionRepository);
		listeners.add(c);
		defaultWebSessionManager.setSessionListeners(listeners);
		
		ExecutorServiceSessionValidationScheduler sessionValidationScheduler = new ExecutorServiceSessionValidationScheduler();
		sessionValidationScheduler.setInterval(1800000);
		sessionValidationScheduler.setSessionManager(defaultWebSessionManager);
		defaultWebSessionManager.setSessionValidationScheduler(sessionValidationScheduler);
		defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
		
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
	
	    //设置authenticator
	    ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
	    authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
	    securityManager.setAuthenticator(authenticator);
	
	    //设置authorizer
	    ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
	    authorizer.setPermissionResolver(new WildcardPermissionResolver());
	    securityManager.setAuthorizer(authorizer);
	
	    //设置Realm
//	    EhCacheManager ehCacheManager = new EhCacheManager();
//	    ehCacheManager.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
	    

	    //UserRealm userRealm = new UserRealm();
	    
	    userRealm.setCredentialsMatcher(new SimpleCredentialsMatcher());
//	    userRealm.setCacheManager(ehCacheManager);
	    //userRealm.setCachingEnabled(true);
//	    userRealm.setAuthenticationCacheName("authenticationCache");
//	    userRealm.setAuthenticationCachingEnabled(true);
//	    userRealm.setAuthorizationCacheName("authorizationCacheName");
//	    userRealm.setAuthorizationCachingEnabled(true);
	    
	    //test for cas sso
	    
	    securityManager.setRealms(Arrays.asList((Realm) userRealm));
	    securityManager.setSessionManager(defaultWebSessionManager);
	    //securityManager.setCacheManager(ehCacheManager);
	
	    //将SecurityManager设置到SecurityUtils 方便全局使用
	    SecurityUtils.setSecurityManager(securityManager);
	    
	    return securityManager;
	}
	
	/*
	@Bean(name="restfilter")
	public HttpMethodPermissionFilter createFilter(){
		return new HttpMethodPermissionFilter();
	}*/
	
	/*
	@Bean
	public FilterRegistrationBean restFilterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		
		HttpMethodPermissionFilter restFilter = new HttpMethodPermissionFilter();
		registrationBean.setFilter(restFilter);
		registrationBean.addUrlPatterns("/rest/*");
		return registrationBean;
		
	}*/
	
	@Bean
	@Autowired
	public AuthorizationAttributeSourceAdvisor advisor(DefaultWebSecurityManager webSecurityManager){
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(webSecurityManager);
		return advisor;
	}
	
	@Bean
	@Autowired
	public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager webSecurityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new org.apache.shiro.spring.web.ShiroFilterFactoryBean();
		
		shiroFilterFactoryBean.setSecurityManager(webSecurityManager);
		
		//add for cas sso
		shiroFilterFactoryBean.setLoginUrl("http://localhost/login?service=http://localhost:8000/aaa/cas");
		shiroFilterFactoryBean.setSuccessUrl("/");
		
		//add cas filter  which will process callback url;
		//shiroFilterFactoryBean.
		
		//Map<String, String> filterChainDefinitionMap = new HashMap<String,String>();
		//filterChainDefinitionMap.put("/rest/**", "restFilter");
		//shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}
	
	/*
	@Bean
	public FilterRegistrationBean restFilterRegistrationBean(){
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		RestFormAuthenticationFilter restFilter = new RestFormAuthenticationFilter();
		registrationBean.setFilter(restFilter);
		registrationBean.setName("restFilter");
		registrationBean.addUrlPatterns("/rest/*");
		return registrationBean;
	}*/
	
	

}

