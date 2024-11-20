package com.ivory.ivory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PageInfo {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
