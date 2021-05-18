package me.weldnor.domain;


public class CashDesk {
    private long balance;

    public CashDesk(long balance) {
        this.balance = balance;
    }

    public synchronized boolean tryMakeOperation(int operationSum, OperationType operationType) {
        if (operationType == OperationType.GET_MONEY && this.balance < operationSum) {
            return false;
        }
        if (operationType == OperationType.GET_MONEY) {
            balance -= operationSum;
            return true;
        }
        // or
        balance += operationSum;
        return true;
    }

    public synchronized long getBalance() {
        return balance;
    }
}
