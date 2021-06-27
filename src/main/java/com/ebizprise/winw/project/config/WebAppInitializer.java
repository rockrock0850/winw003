package com.ebizprise.winw.project.config;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import com.ebizprise.winw.project.security.service.SessionListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.springframework.web.util.IntrospectorCleanupListener;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// Log4jConfigListener
		servletContext.setInitParameter("logbackConfigLocation", "classpath:logback.xml");
		servletContext.addListener(LogbackConfigListener.class);
		// 在springmvc中獲取request的監聽器
		servletContext.addListener(RequestContextListener.class);
		servletContext.addListener(IntrospectorCleanupListener.class);
		servletContext.addListener(SessionListener.class);
		
		super.onStartup(servletContext);
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return new Filter[] { characterEncodingFilter };
	}

}
