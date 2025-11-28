package com.ccblog.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ccblog.result.PageResult;

/**
 * PageResult和IPage的转换
 * @author czc
 * @date 2025-09-27
 */
public final class PageUtil {
    /**
     * Ipage转为PageResult
     * @param iPage
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> toPageResult(IPage<T> iPage) {
        return new PageResult<>(iPage.getTotal(), iPage.getRecords());
    }

    /**
     * PageResult转为Ipage
     * @param pr
     * @param <T>
     * @return
     */
    public static <T> IPage<T> toIPage(PageResult<T> pr,int currentPage,int pageSize) {
        // 当前页码、每页条数这里如果不需要可以传 0
        return new Page<T>().setTotal(pr.getTotal()).setRecords(pr.getRecords()).setCurrent(currentPage).setSize(pageSize);
    }

}