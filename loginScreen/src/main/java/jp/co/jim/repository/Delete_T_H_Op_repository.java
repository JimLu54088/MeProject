package jp.co.jim.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface Delete_T_H_Op_repository {

    public int delete_T_hist_table();
}
