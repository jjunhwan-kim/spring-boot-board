package com.spring.board.web.dto;

import lombok.Getter;

@Getter
public class PageListDto {

    private boolean isSelected;
    private int pageNumber;

    public PageListDto(boolean isSelected, int pageNumber) {
        this.isSelected = isSelected;
        this.pageNumber = pageNumber;
    }
}
