package com.oracle.project.bridgingservice.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter{
	@Value("${rest.timeout}")
	private int timeout;
	@Bean
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(timeout);
		factory.setReadTimeout(timeout);
		RestTemplate rest = new RestTemplate(factory);
		return rest;
	}
	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<>(servlet, "/services/bridging-service/*");
	}
	@Bean(name = "bridging-service")
	public Wsdl11Definition CheckPrimaryNumberWsdl11Definition() {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("bridgingservicePort");
		wsdl11Definition.setLocationUri("/services");
		wsdl11Definition.setTargetNamespace("http://www.telkomsel.co.id/ufo/services/bridging-service/v1.0");
		wsdl11Definition.setRequestSuffix("rq");
		wsdl11Definition.setResponseSuffix("rs");
		wsdl11Definition.setSchemaCollection(BridgingServiceSchema()); //use this for multiple xsd
//		wsdl11Definition.setSchema(BridgingServiceSchema()); //use this for single xsd
		
		return wsdl11Definition;
	}
	@Bean
	public XsdSchemaCollection BridgingServiceSchema() {
		CommonsXsdSchemaCollection schema = new CommonsXsdSchemaCollection(
				new ClassPathResource("xsd/mainbridgingservice.xsd"));
		schema.setInline(true);
		
		return schema;
	}
	
	@Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
		
//        interceptors.add();
    }
}
