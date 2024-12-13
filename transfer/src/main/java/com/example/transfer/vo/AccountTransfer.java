package com.example.transfer.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class AccountTransfer {
    private Integer id;
    // 交易前點數
    private Integer before;
    // 交易後點數
    private Integer after;
}
