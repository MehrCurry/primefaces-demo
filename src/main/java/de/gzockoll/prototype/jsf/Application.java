package de.gzockoll.prototype.jsf;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.sun.faces.config.ConfigureListener;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletListenerRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Slf4j
public class Application extends SpringBootServletInitializer implements ServletContextAware {
    public static final String HAZELCAST_INSTANCE = "theInstance";

    public Application() {
        logger.debug("Initialised Application");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public ServletRegistrationBean facesServletRegistration() {
        ServletRegistrationBean registration = new ServletRegistrationBean(
                new FacesServlet(), "*.xhtml", "*.jsf");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean<ConfigureListener> jsfConfigureListener() {
        return new ServletListenerRegistrationBean<ConfigureListener>(
                new ConfigureListener());
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsf");
        return resolver;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());
        servletContext.setInitParameter("primefaces.FONT_AWESOME", Boolean.TRUE.toString());
        servletContext.setInitParameter("primefaces.UPLOADER", "commons");
        servletContext.setInitParameter("primefaces.THEME", "sunny");
    }

    @Bean
    public FilterRegistrationBean uploadFilter() {
        FilterRegistrationBean bean=new FilterRegistrationBean();
        FileUploadFilter uploadFilter=new FileUploadFilter();
        bean.setFilter(uploadFilter);
        bean.addServletRegistrationBeans(facesServletRegistration());
        bean.setOrder(2);
        return bean;
    }

    @Bean
    public HazelcastInstance getInstance() {
        return Hazelcast.getOrCreateHazelcastInstance(getConfig());

    }

    @Bean
    public Config getConfig() {
        Config config=new Config();
        config.setInstanceName(HAZELCAST_INSTANCE);
        return config;
    }
}