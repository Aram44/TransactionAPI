package com.example.transactionapi.model.transaction;

import org.springframework.data.domain.Sort;

public class TransactionPage {
    private int page = 0;
    private int pageSize = 10;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String sortBy = "id";

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
