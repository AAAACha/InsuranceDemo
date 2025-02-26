package com.schj.controller;

import com.schj.pojo.po.Result;
import com.schj.service.ExcelExporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Descriptioin ExcelExporterController
 * @Author AvA
 * @Date 2025-02-22
 */
@RestController
@RequestMapping("/excel-exporter")
@Slf4j
public class ExcelExporterController {

    @Autowired
    private ExcelExporterService excelExporterService;

    /**
     * 文件导出
     *
     * 导出按照更新时间降序排列的先count条数据道excel文件中(count>0 && count <= 5000)
     * @param count
     * @return
     */
    @GetMapping("/exporter/{count}")
    public Result ExcelExporter(@PathVariable int count){
        try {
            if(count > 0 && count <= 5000){
                excelExporterService.ExcelExporter(count);
                return Result.success();
            } else {
                return  Result.error("导出的数据必须在0-5000条内");
            }
        } catch (Exception e){
          log.error("文件导出失败, 失败原因:"+e);
          return  Result.error("文件导出失败,失败原因,请重试");
        }
    }
}
