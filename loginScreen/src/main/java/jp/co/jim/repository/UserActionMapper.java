package jp.co.jim.repository;

import jp.co.jim.entity.HisUserOperation;
import jp.co.jim.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserActionMapper {
    void insertUserAction(HisUserOperation hisUserOperation);


    UserEntity findByUsername(String username);

}
