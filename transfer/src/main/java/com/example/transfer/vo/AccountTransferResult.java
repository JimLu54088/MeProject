package com.example.transfer.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
//Added by myself started.
@Setter
//Added by myself ended.
@AllArgsConstructor
public class AccountTransferResult {
    // 交易狀態
    private Boolean status;
    // 交易點數
    private Integer point;
    // 轉出
    private AccountTransfer source;
    // 轉入
    private AccountTransfer target;

    //Added by myself started.
    public AccountTransferResult() {

    }
    //Added by myself ended.
}
