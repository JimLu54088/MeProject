package jp.co.jim.service;

import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    @Autowired
    private LoginRepository repository;

    public int checkAdminLogin(String username, String password) {
        return repository.checkLogin(username, password);
    }

    public void insertSearchCriteriaData(SearchCriteriaEntity entity) {
        repository.insertSearchCriteriaData(entity);
    }

    public List<SearchCriteriaEntity> selectCriteriaDataByID(String userId) {
        return repository.selectCriteriaDataByID(userId);
    }

    public void deleteSavedCriteriaByIDAndName(String user_id, String s_c_id){
        repository.deleteSavedCriteriaByIDAndName(user_id,s_c_id );
    }

    public int countOfSavedSearchCriteriaByID(String userId){
       return repository.countOfSavedSearchCriteriaByID(userId);
    }


}
