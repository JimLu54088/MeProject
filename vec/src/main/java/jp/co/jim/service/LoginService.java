package jp.co.jim.service;

import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public void deleteSavedCriteriaByIDAndName(String user_id, String s_c_id) {
        repository.deleteSavedCriteriaByIDAndName(user_id, s_c_id);
    }

    public int countOfSavedSearchCriteriaByID(String userId) {
        return repository.countOfSavedSearchCriteriaByID(userId);
    }


    public List<Map<String, Object>> searchSingleVEC(SearchCriteriaEntity entity) {

        List<String> cmnmnList = getDistinctCMNMN(); // 先查詢 DISTINCT CMNMN

        return repository.searchSingleVEC(entity, cmnmnList);
    }

    public List<String> getDistinctCMNMN() {
        return repository.getDistinctCMNMN();
    }


}
