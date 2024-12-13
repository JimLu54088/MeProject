package com.example.transfer.result;

import com.example.transfer.data.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllResult {
    private List<Account> accounts;
    private Long totalPoint;
}
