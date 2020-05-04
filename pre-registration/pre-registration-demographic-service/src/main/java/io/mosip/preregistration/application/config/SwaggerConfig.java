package io.mosip.preregistration.application.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for swagger config
 * 
 * @author Chandra GKS
 * @since 1.0.0
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * Master service Version
	 */
	private static final String SERVICE_VERSION = "0.0.1";
	/**
	 * Application Title
	 */
	private static final String TITLE = "Preregistration Demographic Service";
	/**
	 * Master Data Service
	 */
	private static final String DESCRIPTION = "Preregistration Demographic Service";

	@Value("${application.env.local:false}")
	private Boolean localEnv;

	@Value("${swagger.base-url:#{null}}")
	private String swaggerBaseUrl;

	@Value("${server.port:8080}")
	private int serverPort;

	String proto = "http";
	String host = "localhost";
	int port = -1;
	String hostWithPort = "localhost:8080";

	/**
	 * Produces {@link ApiInfo}
	 * 
	 * @return {@link ApiInfo}
	 */
	private ApiInfo apiInfo() {
		System.out.println("DEBUG KER AUTH FAILURE 1:Title: "+TITLE+" Description: "+DESCRIPTION+" Service Version: "+SERVICE_VERSION);
		return new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).version(SERVICE_VERSION).build();
	}

	/**
	 * Produce Docket bean
	 * 
	 * @return Docket bean
	 */
	@Bean
	public Docket api() {
		boolean swaggerBaseUrlSet = false;

		System.out.println("DEBUG KER AUTH FAILURE 2:swaggerBaseUrl : "+swaggerBaseUrl );

		if (!localEnv && swaggerBaseUrl != null && !swaggerBaseUrl.isEmpty()) {
			try {
				proto = new URL(swaggerBaseUrl).getProtocol();
				host = new URL(swaggerBaseUrl).getHost();
				port = new URL(swaggerBaseUrl).getPort();
				if (port == -1) {
					hostWithPort = host;
				} else {
					hostWithPort = host + ":" + port;
				}
				swaggerBaseUrlSet = true;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				System.err.println("SwaggerUrlException: " + e);
			}
		}

		Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.paths(PathSelectors.regex("(?!/(error).*).*")).build();

		System.out.println("DEBUG KER AUTH FAILURE 3:swaggerBaseUrlSet: "+swaggerBaseUrlSet);

		if (swaggerBaseUrlSet) {
			docket.protocols(protocols()).host(hostWithPort);
			System.out.println("DEBUG KER AUTH FAILURE 6:hostWithPort: "+hostWithPort);
			System.out.println("\nSwagger Base URL: " + proto + "://" + hostWithPort + "\n");
		}

		return docket;
	}

	private Set<String> protocols() {
		Set<String> protocols = new HashSet<>();
		System.out.println("DEBUG KER AUTH FAILURE 5:protocols : "+protocols );
		protocols.add(proto);
		return protocols;
	}
}
