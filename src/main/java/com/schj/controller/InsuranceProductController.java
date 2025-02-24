package com.schj.controller;


import com.schj.pojo.dto.request.InsuranceProductReqDTO;
import com.schj.pojo.dto.response.InsuranceProductResDTO;
import com.schj.pojo.po.Result;
import com.schj.service.InsuranceProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/insurance-product")
@Slf4j
public class InsuranceProductController {

    @Autowired
    private InsuranceProductService insuranceProductService;

    /**
     * 新增险种信息
     *
     * @param insuranceProductReqDTO
     * @return
     */
    @PostMapping("/insertInsuranceProduct")
    public Result insertInsuranceProduct(@RequestBody InsuranceProductReqDTO insuranceProductReqDTO) {
        try {
            if (insuranceProductReqDTO == null) {
                log.error("新增险种失败,失败原因为:请求体为空");
                return Result.error("新增失败");
            }
            insuranceProductService.insertInsuranceProduct(insuranceProductReqDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("新增险种失败,失败原因为:" + e);
            return Result.error("插入失败");
        }
    }

    /**
     * 根据id获取险种信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getInsuranceProductById/{id}")
    public Result getInsuranceProductById(@PathVariable Long id) {
        try {
            InsuranceProductResDTO result = insuranceProductService.getInsuranceProductById(id);
            if (result != null) {
                return Result.success(result);
            } else {
                log.error("查询险种失败,失败原因为:未找到对应id的险种信息");
                return Result.error("查询失败");
            }
        } catch (Exception e) {
            log.error("查询险种失败,失败原因为:" + e);
            return Result.error("查询失败");
        }
    }

    /**
     * 根据id修改险种信息
     *
     * @param insuranceProductReqDTO
     * @return
     */
    @PutMapping("/updateInsuranceProductById/{id}")
    public Result updateInsuranceProductById(@PathVariable Long id, @RequestBody InsuranceProductReqDTO insuranceProductReqDTO) {
        try {
            if (insuranceProductReqDTO == null) {
                log.error("修改险种失败,失败原因为:请求体为空");
                return Result.error("修改失败");
            }
            insuranceProductService.updateInsuranceProductById(id, insuranceProductReqDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("修改险种失败,失败原因为:" + e);
            return Result.error("修改失败");
        }
    }

    /**
     * 根据id逻辑删除险种信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsuranceProductById/{id}")
    public Result deleteInsuranceProductById(@PathVariable Long id) {
        try {
            insuranceProductService.deleteInsuranceProductById(id);
                return Result.success();
        } catch (Exception e) {
            log.error("删除险种失败,失败原因为:" + e);
            return Result.error("删除失败");
        }
    }

}
