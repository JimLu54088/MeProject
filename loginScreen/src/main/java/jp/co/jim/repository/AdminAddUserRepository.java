package jp.co.jim.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminAddUserRepository {
    public int checkinsertingExisting(String username);

    public void insertUser(String username, String password);


}
