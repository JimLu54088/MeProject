package jp.co.jim.repository;

import jp.co.jim.entity.T_basic_kuruma_info_entity;
import jp.co.jim.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HandlerJIM5PRequestRepository {

    T_basic_kuruma_info_entity getAllByFin(String fin_number);

}
