package com.schj.controller;


import com.schj.pojo.dto.request.InsurancePolicyReqDTO;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.po.Result;
import com.schj.service.InsurancePolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/insurance-policy")
@Slf4j
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService insurancePolicyService;

    /**
     * 插入新的保险单信息
     * @param insurancePolicyReqDTO
     * @return
     */
    @PostMapping("/insertInsurancePolicy")
    public Result insertInsurancePolicy(@RequestBody InsurancePolicyReqDTO insurancePolicyReqDTO) {
        if (insurancePolicyReqDTO == null) {
            log.error("插入失败,请求体不能为空");
            return Result.error("新增失败,请检查您录入的信息");
        }
        insurancePolicyService.insertInsurancePolicy(insurancePolicyReqDTO);
        return Result.success();
    }

    /**
     * 根据ID获取保险单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getInsurancePolicyById/{id}")
    public Result getInsurancePolicyById(@PathVariable Long id) {
        if (id == null) {
            log.error("ID不能为空");
            return Result.error("查询失败, 您查询的保单不存在");
        }
        return Result.success(insurancePolicyService.getInsurancePolicyById(id));
    }

    /**
     * 根据ID更新保险单信息
     *
     * @param id
     * @param insurancePolicyReqDTO
     * @return
     */
    @PutMapping("/updateInsurancePolicyById/{id}")
    public Result updateInsurancePolicyById(@PathVariable Long id, @RequestBody InsurancePolicyReqDTO insurancePolicyReqDTO) {
        if (id == null || insurancePolicyReqDTO == null) {
            log.error("修改失败,失败原因:ID和请求体不能为空");
            return Result.error("修改失败,请检查您录入的信息");
        }
        insurancePolicyService.updateInsurancePolicyById(id, insurancePolicyReqDTO);
        return Result.success();
    }

    /**
     * 根据ID删除保险单信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsurancePolicyById/{id}")
    public Result deleteInsurancePolicyById(@PathVariable Long id) {
        if (id == null) {
            log.error("删除失败,失败原因:ID不能为空");
            return Result.error("删除失败,您要删除的保单不存在");
        }
        insurancePolicyService.deleteInsurancePolicyById(id);
        return Result.success();
    }

    /**
     * 保单信息分页查询
     *
     * @param policyQueryRequest
     * @return
     */
    @GetMapping("/search")
    public Result searchPolicies(@RequestBody PolicyQueryRequest policyQueryRequest) {
        if (policyQueryRequest == null) {
            log.error("分页查询失败, 失败原因: 请求体为空");
            return Result.error("查询失败,请检查您的查询条件");
        }
        return Result.success(insurancePolicyService.getPoliciesByCondition(policyQueryRequest));
    }
}
