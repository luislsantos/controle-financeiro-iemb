package com.example.testefrontiemb.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
@Component
public class CustomDateTimeFormatter {
    public DateTimeFormatter dateFormatter;
    @Autowired
    public CustomDateTimeFormatter() {
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");;
    }
}
