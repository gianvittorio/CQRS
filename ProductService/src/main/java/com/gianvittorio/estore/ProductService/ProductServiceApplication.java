package com.gianvittorio.estore.ProductService;

import com.gianvittorio.estore.ProductService.command.interceptor.CreateProductCommandInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	@Autowired
	public void registerCreateProductCommandInterceptor(ApplicationContext applicationContext, CommandBus commandBus) {
		commandBus.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptor.class));
	}
}
