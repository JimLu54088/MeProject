package jp.co.jim.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface  AdminLoginRepository {
    public int checkAdminLogin(String username, String password);
}
