package com.schj.controller;


import com.schj.pojo.dto.request.InsuranceCompanyReqDTO;
import com.schj.pojo.dto.response.InsuranceCompanyResDTO;
import com.schj.pojo.po.Result;
import com.schj.service.InsuranceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/insurance-company")
public class InsuranceCompanyController {

    @Autowired
    InsuranceCompanyService insuranceCompanyService;

    /**
     * 新增保险公司
     * @param insuranceCompanyReqDTO
     * @return
     */
    @PostMapping("/insertInsuranceCompany")
    public Result insertInsuranceCompany(@RequestBody InsuranceCompanyReqDTO insuranceCompanyReqDTO){
        Boolean result = insuranceCompanyService.insertInsuranceCompany(insuranceCompanyReqDTO);
        if(result){
            return Result.success();
        } else {
            return Result.error("插入失败");
        }
    }

    /**
     * 根据id获取保险公司
     * @param id
     * @return
     */
    @GetMapping("/getInsuranceCompanyById/{id}")
    public Result getInsuranceCompanyById(@PathVariable Long id){
        InsuranceCompanyResDTO result = insuranceCompanyService.getInsuranceCompanyById(id);
        if(result != null){
            return Result.success(result);
        } else {
            return Result.error("查询失败");
        }
    }

    /**
     * 根据id修改保险公司
     * @param id
     * @param insuranceCompanyReqDTO
     * @return
     */
    @PutMapping("/updateInsuranceCompanyById/{id}")
    public Result updateInsuranceCompanyById(@PathVariable Long id, @RequestBody InsuranceCompanyReqDTO insuranceCompanyReqDTO){
        Boolean result = insuranceCompanyService.updateInsuranceCompanyById(id, insuranceCompanyReqDTO);
        if(result){
            return Result.success();
        } else {
            return Result.error("插入失败");
        }
    }

    /**
     * 根据id逻辑删除保险公司
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsuranceCompanyById/{id}")
    public Result deleteInsuranceCompanyById(@PathVariable Long id){
        Boolean result = insuranceCompanyService.deleteInsuranceCompanyById(id);
        if(result){
            return Result.success();
        } else {
            return Result.error("插入失败");
        }
    }
}
