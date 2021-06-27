package com.ebizprise.winw.project.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.ebizprise.project.utility.doc.velocity.VelocityUtil;
import com.ebizprise.winw.project.config.formatter.DateFormatter;
import com.ebizprise.winw.project.interceptor.GroupPermissionInterceptor;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan(basePackages = { "com.ebizprise.winw" })
@PropertySource({ "classpath:resources.properties", "classpath:datasource.properties" })
@EnableWebMvc
@EnableScheduling
public class AppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;
	
	@Bean
    public TaskExecutor threadPoolTaskExecutor () {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
        
        return executor;
    }
    
    @Override
    public void addFormatters (FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter());
    }

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:message");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setDefaultLocale(Locale.TAIWAN);
		resolver.setCookieName("localeCookie");
		resolver.setCookieMaxAge(4800);
		return resolver;
	}

	/**
	 * an interceptor bean that will switch to a new locale based on the value of the language parameter appended to a request:
	 *
	 * @param registry
	 * @language should be the name of the request param i.e  localhost:8010/api/get-greeting?language=fr
	 * <p>
	 * Note: All requests to the backend needing Internationalization should have the "language" request param
	 */
	@Override
	public void addInterceptors (InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		
        InterceptorRegistration registration = registry.addInterceptor(groupPermissionInterceptor());
        registration.addPathPatterns("/**");
        registration.excludePathPatterns(excludes());// 有需要撈非選單路徑的資料，須在下面新增排除路徑，否則會被擋掉
        
        super.addInterceptors(registry);
	}

    public LocaleChangeInterceptor localeChangeInterceptor () {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
	}
	
    @Bean
    public GroupPermissionInterceptor groupPermissionInterceptor() {
        return new GroupPermissionInterceptor();
    }

	@Bean
	public VelocityUtil velocityUtil() {
		VelocityUtil velocityUtil = new VelocityUtil();
		velocityUtil.setDir(env.getProperty("template.path"));
		velocityUtil.initClassPath();
		return velocityUtil;
	}

	/**
	 * Configure ResourceHandlers to serve static resources like CSS/ Javascript
	 * etc...
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
    @Bean
    public MultipartResolver multipartResolver() {
       return new CommonsMultipartResolver();
    }

    private String[] excludes () {
        List<String> excludes = new ArrayList<>();
        excludes.add("/ws/**");
        excludes.add("/login");
        excludes.add("/menu/**");
        excludes.add("/html/**");
        excludes.add("/dashboard");
        
        String[] array = new String[excludes.size()];
        
        return excludes.toArray(array);
    }
	
}