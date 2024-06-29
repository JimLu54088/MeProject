package jp.co.jim.service;

import jp.co.jim.repository.AdminLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginService {

    @Autowired
    private AdminLoginRepository adminLoginRepository;

    public int checkAdminLogin(String username, String password) {
        return adminLoginRepository.checkAdminLogin(username, password);
    }
}
