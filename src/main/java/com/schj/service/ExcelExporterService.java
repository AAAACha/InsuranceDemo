package com.schj.service;

import org.springframework.stereotype.Service;

/**
 * @Descriptioin ExcelExporterService
 * @Author AvA
 * @Date 2025-02-22
 */
@Service
public interface ExcelExporterService {
    /**
     * 文件导出
     * @param count
     */
    void ExcelExporter(int count);
}
