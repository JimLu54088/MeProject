package com.example.transfer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;


@Configuration
public class AccountConfig implements CommandLineRunner {
    public static final int AccountCount = 10;
    public static final int DefaultPoint = 1000;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        jdbcTemplate.execute((ConnectionCallback<Object>) connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into account(point) values(?)");
            for (int i = 1; i <= AccountCount; i++) {
                ps.setInt(1, DefaultPoint);
                ps.executeUpdate();
            }
            return null;
        });
    }
}
