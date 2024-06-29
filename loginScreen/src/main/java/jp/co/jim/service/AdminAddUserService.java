package jp.co.jim.service;

import jp.co.jim.repository.AdminAddUserRepository;
import jp.co.jim.repository.AdminLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminAddUserService {

    @Autowired
    private AdminAddUserRepository adminAddUserRepository;

    public int checkinsertingExisting(String username) {
        return adminAddUserRepository.checkinsertingExisting(username);
    }

    public void insertUser(String username, String password){
        adminAddUserRepository.insertUser(username, password);
    }


}
