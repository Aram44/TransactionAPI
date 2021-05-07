package com.example.transactionapi.model.loan;

public enum Status {
    PROCESS,
    DONE,
    REFUSED,
    CANCELED,
    EDITED;

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
            case 4:
                return EDITED;
            default:
                return null;
        }
    }
}
