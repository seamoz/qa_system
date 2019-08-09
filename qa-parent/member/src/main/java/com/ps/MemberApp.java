package com.ps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Hello world!
 *
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.ps.mapper","com.ps.serviceImpl"})
public class MemberApp {
    public static void main( String[] args ) {

        SpringApplication.run(MemberApp.class,args);

    }
}
