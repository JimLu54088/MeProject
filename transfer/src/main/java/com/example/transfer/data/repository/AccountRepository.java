package com.example.transfer.data.repository;

import com.example.transfer.data.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("select point from Account where id=:id")
    public Integer findPoint(Integer id);

    @Transactional
    @Modifying
    @Query("update Account set point = point-:point where id=:id and point>=:point")
    public int outPoint(Integer id, Integer point);

    @Transactional
    @Modifying
    @Query("update Account set point = point+:point where id=:id")
    public int inPoint(Integer id, Integer point);
}
