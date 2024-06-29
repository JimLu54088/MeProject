package jp.co.jim.service;

import jp.co.jim.entity.HisUserOperation;
import jp.co.jim.entity.UserEntity;
import jp.co.jim.repository.UserActionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionService {

    @Autowired
    private UserActionMapper userActionMapper;

    public void saveUserAction(String userId, String details) {
        HisUserOperation hisUserOperation = new HisUserOperation();
        hisUserOperation.setUSER_ID(userId);
        hisUserOperation.setUSER_OPERATION_DETAILS(details);
        userActionMapper.insertUserAction(hisUserOperation);
    }

//    public List<UserAction> getActionsByUserId(Long userId) {
//        return userActionMapper.getUserActionsByUserId(userId);
//    }
     public UserEntity findByUsername(String username){
        return userActionMapper.findByUsername(username);
     }


}
