package com.example.testefrontiemb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TesteFrontIembApplication {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(TesteFrontIembApplication.class, args);
        /*TelaPrincipal telaPrincipal = new TelaPrincipal();
        telaPrincipal.exibir();*/
    }

}
