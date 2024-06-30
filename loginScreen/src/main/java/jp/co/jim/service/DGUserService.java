package jp.co.jim.service;

import jp.co.jim.entity.UserEntity;
import jp.co.jim.repository.UserActionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class DGUserService {

    @Autowired
    private UserActionMapper uerActionMapper;

    @Value("${daysSinceUpdateSetting}")
    private String daysSinceUpdateSetting;

    public String handleLogin(String username, String password) {
        UserEntity user = uerActionMapper.findByUsername(username);

        if (user == null || !user.getUser_password().equals(password)) {
            return "DGRP001";
        }

        LocalDateTime updateDate = user.getUpd_dt();
        if (updateDate == null) {
            // First time login, force password update
            return "DGRP002";
        }

        long daysSinceUpdate = ChronoUnit.DAYS.between(updateDate, LocalDateTime.now());
       int intDaysSinceUpdateSetting = Integer.valueOf(daysSinceUpdateSetting);
        if (daysSinceUpdate > intDaysSinceUpdateSetting) {
            return "Login successful! Please update your password within 7 days.";
        } else {
            return "DGRP000";
        }


    }
    public void updateDGUserPassword(UserEntity user){
        uerActionMapper.updateDGUserPassword(user);
    }


}
