# 交易測驗

### 以下題目需考慮有做負載均衡的情況

## 題目一

實作 AccountService.transfer 轉點功能

### 測試情境

從 10 個帳戶中，隨機一個帳戶轉出點數到另一個帳戶

API：account/transfer/test

### 期望

數萬次交易後，所有帳戶的加總金額不變

## 題目二

實作 AccountService.transfer2 轉點功能，返回當下交易的點數狀態

### 測試情境

從 10 個帳戶中，隨機一個帳戶轉出點數到另一個帳戶

API：account/transfer2/test

### 期望

當同一個帳號同時多次交易時，返回結果不會受到同時間，其他交易結果的影響