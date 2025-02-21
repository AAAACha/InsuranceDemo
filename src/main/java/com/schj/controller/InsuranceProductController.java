package com.schj.controller;


import com.schj.pojo.dto.request.InsuranceProductReqDTO;
import com.schj.pojo.dto.response.InsuranceProductResDTO;
import com.schj.pojo.po.Result;
import com.schj.service.InsuranceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/insurance-product")
public class InsuranceProductController {

    @Autowired
    private InsuranceProductService insuranceProductService;

    /**
     * 新增险种信息
     * @param insuranceProductReqDTO
     * @return
     */
    @PostMapping("/insertInsuranceCompany")
    public Result insertInsuranceCompany(@RequestBody InsuranceProductReqDTO insuranceProductReqDTO){
        Boolean result = insuranceProductService.insertInsuranceProduct(insuranceProductReqDTO);
        if(result){
            return Result.success();
        } else {
            return Result.error("插入失败");
        }
    }

    /**
     * 根据id获取险种信息
     * @param id
     * @return
     */
    @GetMapping("/getInsuranceCompanyById/{id}")
    public Result getInsuranceCompanyById(@PathVariable Long id){
        InsuranceProductResDTO result = insuranceProductService.getInsuranceProductById(id);
        if(result != null){
            return Result.success(result);
        } else {
            return Result.error("查询失败");
        }
    }

    /**
     * 根据id修改险种信息
     * @param insuranceProductReqDTO
     * @return
     */
    @PutMapping("/updateInsuranceCompanyById")
    public Result updateInsuranceCompanyById(@RequestBody InsuranceProductReqDTO insuranceProductReqDTO){
        Boolean result = insuranceProductService.updateInsuranceProductById(insuranceProductReqDTO);
        if(result){
            return Result.success();
        } else {
            return Result.error("插入失败");
        }
    }

    /**
     * 根据id逻辑删除险种信息
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsuranceCompanyById/{id}")
    public Result deleteInsuranceCompanyById(@PathVariable Long id){
        Boolean result = insuranceProductService.deleteInsuranceProductById(id);
        if(result){
            return Result.success();
        } else {
            return Result.error("插入失败");
        }
    }
}
