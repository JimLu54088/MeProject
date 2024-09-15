package jp.co.jim.service;

import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.SearchResultEntity;
import jp.co.jim.repository.LoginRepository;
import jp.co.jim.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchResultService {

    @Autowired
    private SearchResultRepository repository;


    public List<SearchResultEntity> selectSearchResultByID(String userId) {
        return repository.selectSearchResultByID(userId);
    }

    public void deleteSavedsearchResult(String user_id, String s_r_id) {
        repository.deleteSavedsearchResult(user_id, s_r_id);
    }

    public void saveSearchResultIntoDB(SearchResultEntity entity) {
        repository.saveSearchResultIntoDB(entity);
    }


}
