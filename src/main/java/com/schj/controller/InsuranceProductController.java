package com.schj.controller;


import com.schj.pojo.dto.request.InsuranceProductReqDTO;
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
        if (insuranceProductReqDTO == null) {
            log.error("新增险种失败,失败原因为:请求体为空");
            return Result.error("新增失败,请检查您录入的信息");
        }
        insuranceProductService.insertInsuranceProduct(insuranceProductReqDTO);
        return Result.success();
    }

    /**
     * 根据id获取险种信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getInsuranceProductById/{id}")
    public Result getInsuranceProductById(@PathVariable Long id) {
        if(id != null){
            return Result.success(insuranceProductService.getInsuranceProductById(id));
        } else {
            return Result.error("查询失败,您查询的险种不存在");
        }
    }

    /**
     * 根据id修改险种信息
     *
     * @param id
     * @param insuranceProductReqDTO
     * @return
     */
    @PutMapping("/updateInsuranceProductById/{id}")
    public Result updateInsuranceProductById(@PathVariable Long id, @RequestBody InsuranceProductReqDTO insuranceProductReqDTO) {
        if (insuranceProductReqDTO == null) {
            log.error("修改险种失败,失败原因为:请求体为空");
            return Result.error("修改失败,请检查您录入的信息");
        }
        insuranceProductService.updateInsuranceProductById(id, insuranceProductReqDTO);
        return Result.success();
    }

    /**
     * 根据id逻辑删除险种信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsuranceProductById/{id}")
    public Result deleteInsuranceProductById(@PathVariable Long id) {
        if (id == null) {
            log.error("删除险种失败,失败原因为:id为空");
            return Result.error("删除失败,您删除的险种信息不存在");
        }
        insuranceProductService.deleteInsuranceProductById(id);
        return Result.success();
    }

}
