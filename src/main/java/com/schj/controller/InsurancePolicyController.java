package com.schj.controller;


import com.schj.pojo.dto.request.InsurancePolicyReqDTO;
import com.schj.pojo.dto.request.PolicyQueryRequest;
import com.schj.pojo.dto.response.InsurancePolicyResDTO;
import com.schj.pojo.po.PageBean;
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
    public Result insertInsurancePolicy(@RequestBody InsurancePolicyReqDTO insurancePolicyReqDTO){
        try {
            if (insurancePolicyReqDTO == null) {
                return Result.error("请求体不能为空");
            }
            insurancePolicyService.insertInsurancePolicy(insurancePolicyReqDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("插入保险单时出错: " + e.getMessage());
            return Result.error("插入保险单失败");
        }
    }

    /**
     * 根据ID获取保险单信息
     * @param id
     * @return
     */
    @GetMapping("/getInsurancePolicyById/{id}")
    public Result getInsurancePolicyById(@PathVariable Long id){
        try {
            if (id == null) {
                return Result.error("ID不能为空");
            }
            InsurancePolicyResDTO insurancePolicyResDTO = insurancePolicyService.getInsurancePolicyById(id);
            if (insurancePolicyResDTO == null) {
                return Result.error("未找到保险单");
            }
            return Result.success(insurancePolicyResDTO);
        } catch (Exception e) {
            log.error("根据ID获取保险单时出错: " + e.getMessage());
            return Result.error("获取保险单失败");
        }
    }

    /**
     * 根据ID更新保险单信息
     * @param id
     * @param insurancePolicyReqDTO
     * @return
     */
    @PutMapping("/updateInsurancePolicyById/{id}")
    public Result updateInsurancePolicyById(@PathVariable Long id, @RequestBody InsurancePolicyReqDTO insurancePolicyReqDTO){
        try {
            if (id == null || insurancePolicyReqDTO == null) {
                return Result.error("ID和请求体不能为空");
            }
            insurancePolicyService.updateInsurancePolicyById(id, insurancePolicyReqDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("更新保险单时出错: " + e.getMessage());
            return Result.error("更新保险单失败");
        }
    }

    /**
     * 根据ID删除保险单信息
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsurancePolicyById/{id}")
    public Result deleteInsurancePolicyById(@PathVariable Long id){
        try {
            if (id == null) {
                return Result.error("ID不能为空");
            }
            insurancePolicyService.deleteInsurancePolicyById(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除保险单时出错: " + e.getMessage());
            return Result.error("删除保险单失败");
        }
    }

    @GetMapping("/search")
    public Result searchPolicies(@RequestBody PolicyQueryRequest policyQueryRequest) {
        try {
            if(policyQueryRequest == null){
                return Result.error("请求体不能为空");
            }
            PageBean pageBean = insurancePolicyService.getPoliciesByCondition(policyQueryRequest);
            return Result.success(pageBean);
        } catch (Exception e){
            log.error("分页查询保险单时出错: " + e.getMessage());
            return Result.error("分页查询保险单失败");
        }
    }
}
