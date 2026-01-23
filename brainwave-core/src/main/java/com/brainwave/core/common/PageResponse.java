package com.brainwave.core.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/**
 * 通用分頁回應 DTO
 * 標準化的分頁回應格式，適用於所有分頁查詢
 *
 * @param <T> 資料類型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    /**
     * 資料列表
     */
    private List<T> content;

    /**
     * 當前頁碼（從 0 開始）
     */
    private int currentPage;

    /**
     * 每頁筆數
     */
    private int pageSize;

    /**
     * 總筆數
     */
    private long totalItems;

    /**
     * 總頁數
     */
    private int totalPages;

    /**
     * 是否有上一頁
     */
    private boolean hasPrevious;

    /**
     * 是否有下一頁
     */
    private boolean hasNext;

    /**
     * 是否為第一頁
     */
    private boolean first;

    /**
     * 是否為最後一頁
     */
    private boolean last;

    /**
     * 從 Spring Data Page 轉換為 PageResponse
     *
     * @param page Spring Data Page 物件
     * @param <T> 資料類型
     * @return PageResponse
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasPrevious(page.hasPrevious())
                .hasNext(page.hasNext())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}
