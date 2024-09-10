package jp.co.jim.repository;

import jp.co.jim.entity.SearchCriteriaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface LoginRepository {

    public int checkLogin(String username, String password);


    public void insertSearchCriteriaData(SearchCriteriaEntity entity);

    public List<SearchCriteriaEntity> selectCriteriaDataByID(String userId);

    public void deleteSavedCriteriaByIDAndName(String user_id, String s_c_id);

    public int countOfSavedSearchCriteriaByID(String userId);


    public List<Map<String, Object>> searchSingleVEC(SearchCriteriaEntity entity, List<String> cmnmnList);

    public List<String> getDistinctCMNMN();


}
