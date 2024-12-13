package com.example.transfer.service;

import com.example.transfer.data.entity.Account;
import com.example.transfer.data.repository.AccountRepository;
import com.example.transfer.vo.AccountTransfer;
import com.example.transfer.vo.AccountTransferResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    private final ConcurrentHashMap<Integer, Object> accountLocks = new ConcurrentHashMap<>();

    private Object getLockForAccount(Integer accountId) {
        return accountLocks.computeIfAbsent(accountId, key -> new Object());
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    /**
     * @param source 轉出
     * @param target 轉入
     * @param point  點數
     */
    @Transactional
    public void transfer(
            Integer source
            , Integer target
            , Integer point
    ) {

        if (source.equals(target)) {
            return;
        }

        // 鎖定來源和目標帳戶，避免多執行緒競爭
        Object sourceLock = getLockForAccount(source);
        Object targetLock = getLockForAccount(target);

        // 確保鎖的順序一致，避免死鎖
        Object firstLock = source < target ? sourceLock : targetLock;
        Object secondLock = source < target ? targetLock : sourceLock;

        synchronized (firstLock) {
            synchronized (secondLock) {
                // 檢查來源餘額是否足夠
                Integer sourcePoint = accountRepository.findPoint(source);
                if (sourcePoint == null || sourcePoint < point) {
                    throw new RuntimeException("來源帳戶餘額不足或不存在");
                }

                // 確認目標帳戶存在
                Integer targetPoint = accountRepository.findPoint(target);
                if (targetPoint == null) {
                    throw new RuntimeException("目標帳戶不存在");
                }

                // 更新來源帳戶餘額
                int updatedSource = accountRepository.outPoint(source, point);
                if (updatedSource == 0) {
                    throw new RuntimeException("扣款失敗");
                }

                // 更新目標帳戶餘額
                accountRepository.inPoint(target, point);
            }
        }

        // 清理無用的鎖
        accountLocks.remove(source);
        accountLocks.remove(target);


        // 1. 驗證參數是否合理
//        if (source == null || target == null || point == null || point <= 0) {
//            throw new IllegalArgumentException("參數不合法，請確認來源、目標和點數是否正確");
//        }
//        if (source.equals(target)) {
//            throw new IllegalArgumentException("來源帳戶和目標帳戶不能相同");
//        }

        // 2. 確認來源帳戶是否有足夠的餘額
//        Integer sourceBalance = accountRepository.findPoint(source);
//        if (sourceBalance == null || sourceBalance < point) {
//            throw new IllegalStateException("來源帳戶餘額不足");
//        }

        // 3. 扣減來源帳戶點數
//        int outResult = accountRepository.outPoint(source, point);
//        if (outResult == 0) {
//            throw new IllegalStateException("來源帳戶扣款失敗，可能餘額不足或帳戶不存在");
//        }

        // 4. 增加目標帳戶點數
//        int inResult = accountRepository.inPoint(target, point);
//        if (inResult == 0) {
//            throw new IllegalStateException("目標帳戶充值失敗，可能帳戶不存在");
//        }
    }
    @Transactional
    public AccountTransferResult transfer2(
            Integer source
            , Integer target
            , Integer point
    ) {
        Integer sourceBeforePoint = 0, sourceAfterPoint = 0, targetBeforePoint = 0, targetAfterPoint = 0;
        if (source.equals(target)) {
//            throw new IllegalArgumentException("來源與目標帳戶不能相同");
            AccountTransferResult accountTransferResult =  new AccountTransferResult();
            accountTransferResult.setStatus(false);
            return accountTransferResult;
        }

        // 鎖定來源和目標帳戶，避免多執行緒競爭
        Object sourceLock = getLockForAccount(source);
        Object targetLock = getLockForAccount(target);

        // 確保鎖的順序一致，避免死鎖
        Object firstLock = source < target ? sourceLock : targetLock;
        Object secondLock = source < target ? targetLock : sourceLock;

//        int sourceBeforePoint, sourceAfterPoint, targetBeforePoint, targetAfterPoint;

        synchronized (firstLock) {
            synchronized (secondLock) {
                // 取得來源帳戶的點數
                 sourceBeforePoint = accountRepository.findPoint(source);
                if (sourceBeforePoint == null || sourceBeforePoint < point) {
//                    throw new RuntimeException("來源帳戶餘額不足或不存在");
                    AccountTransferResult accountTransferResult =  new AccountTransferResult();
                    accountTransferResult.setStatus(false);
                    return accountTransferResult;
                }

                // 取得目標帳戶的點數
                targetBeforePoint = accountRepository.findPoint(target);
                if (targetBeforePoint == null) {
//                    throw new RuntimeException("目標帳戶不存在");
                    AccountTransferResult accountTransferResult =  new AccountTransferResult();
                    accountTransferResult.setStatus(false);
                    return accountTransferResult;
                }

                // 更新來源帳戶餘額
                int updatedSource = accountRepository.outPoint(source, point);
                if (updatedSource == 0) {
//                    throw new RuntimeException("扣款失敗");
                    AccountTransferResult accountTransferResult =  new AccountTransferResult();
                    accountTransferResult.setStatus(false);
                    return accountTransferResult;
                }

                // 更新目標帳戶餘額
                int updatedTarget = accountRepository.inPoint(target, point);

                // 記錄交易後的餘額
                sourceAfterPoint = sourceBeforePoint - point;
                targetAfterPoint = targetBeforePoint + point;
            }
        }

        // 清理無用的鎖
        accountLocks.remove(source);
        accountLocks.remove(target);

        return new AccountTransferResult(
                true
                , point
                , new AccountTransfer(source, sourceBeforePoint, sourceAfterPoint)
                , new AccountTransfer(target, targetBeforePoint, targetAfterPoint)
        );
    }
}
