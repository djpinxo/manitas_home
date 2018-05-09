package com.manitas_home;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class ManitasHomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManitasHomeApplication.class, args);
	}
}

@Configuration
class ConnectorConfig {
	 
	 @Bean
	 public EmbeddedServletContainerFactory servletContainer() {
	 TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
	 @Override
	 protected void postProcessContext(Context context) {
	 SecurityConstraint securityConstraint = new SecurityConstraint();
	 securityConstraint.setUserConstraint("CONFIDENTIAL");
	 SecurityCollection collection = new SecurityCollection();
	 collection.addPattern("/*");
	 securityConstraint.addCollection(collection);
	 context.addConstraint(securityConstraint);
	 }
	 };
	 tomcat.addAdditionalTomcatConnectors(getHttpConnector());
	 return tomcat;
	 }
	 
	 private Connector getHttpConnector() {
	 Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
	 connector.setScheme("http");
	 connector.setPort(80);
	 connector.setSecure(false);
	 connector.setRedirectPort(443);
	 return connector;
	 }
	}