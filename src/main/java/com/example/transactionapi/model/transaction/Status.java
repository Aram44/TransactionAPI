package com.example.transactionapi.model.transaction;

public enum Status {
    PROCESS,
    DONE,
    REFUSED,
    CANCELED;

    public static Status ofIntegerStatus(int status){
        switch (status)
        {
            case 0:
                return PROCESS;
            case 1:
                return DONE;
            case 2:
                return REFUSED;
            case 3:
                return CANCELED;
            default:
                return null;
        }
    }
}
