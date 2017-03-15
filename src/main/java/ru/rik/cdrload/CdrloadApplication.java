package ru.rik.cdrload;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ru.rik.cdrload.db.RouteRepo;

@SpringBootApplication(scanBasePackages = { "ru.rik.cdrload.db" })
@PropertySource({"classpath:application.properties"})
public class CdrloadApplication implements ApplicationRunner {
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CdrloadApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
	
	@Value("${spring.datasource.url}")	private String url;
	@Value("${spring.datasource.username}")	private String user;
	@Value("${spring.datasource.password}")	private String pass;
	
	
	@Bean 
	public DataSource dataSourceTarget() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(url);
		ds.setUsername(user);
		ds.setPassword(pass);
		return ds;
	}
	
	@Autowired	RouteRepo routes;
	

	@Override 
	public void run(ApplicationArguments args) throws Exception {
		for (String a : args.getNonOptionArgs())
			System.out.println(a + ""); 
	} 

}
