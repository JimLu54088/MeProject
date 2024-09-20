package jp.co.jim.repository;

import jp.co.jim.entity.SearchCriteriaEntity;
import jp.co.jim.entity.SearchResultEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SearchResultRepository {

    public List<SearchResultEntity> selectSearchResultByID(String userId);

    public void deleteSavedsearchResult(String user_id, String s_r_id, String ins_dt);


    public void saveSearchResultIntoDB(SearchResultEntity entity);

}
