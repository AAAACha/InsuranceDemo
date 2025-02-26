package com.schj.controller;


import com.schj.pojo.dto.request.InsuranceCompanyReqDTO;
import com.schj.pojo.po.Result;
import com.schj.service.InsuranceCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/insurance-company")
@Slf4j
public class InsuranceCompanyController {

    @Autowired
    InsuranceCompanyService insuranceCompanyService;

    /**
     * 新增保险公司
     *
     * @param insuranceCompanyReqDTO
     * @return
     */
    @PostMapping("/insertInsuranceCompany")
    public Result insertInsuranceCompany(@RequestBody InsuranceCompanyReqDTO insuranceCompanyReqDTO) {
        if (insuranceCompanyReqDTO == null) {
            log.error("新增公司失败,失败原因:请求体为空");
            return Result.error("请求体不得为空");
        }
        insuranceCompanyService.insertInsuranceCompany(insuranceCompanyReqDTO);
        return Result.success();
    }

    /**
     * 根据id获取保险公司
     *
     * @param id
     * @return
     */
    @GetMapping("/getInsuranceCompanyById/{id}")
    public Result getInsuranceCompanyById(@PathVariable Long id) {
        if (id == null) {
            log.error("查询公司失败,失败原因:id为空");
            return Result.error("您查询的保司不存在");
        }
            return Result.success(insuranceCompanyService.getInsuranceCompanyById(id));
    }

    /**
     * 根据id修改保险公司
     *
     * @param id
     * @param insuranceCompanyReqDTO
     * @return
     */
    @PutMapping("/updateInsuranceCompanyById/{id}")
    public Result updateInsuranceCompanyById(@PathVariable Long id, @RequestBody InsuranceCompanyReqDTO insuranceCompanyReqDTO) {
        if (id == null || insuranceCompanyReqDTO == null) {
            log.error("修改公司失败,失败原因:请求体为空");
            return Result.error("修改失败,请检查您录入的信息");
        }
        insuranceCompanyService.updateInsuranceCompanyById(id, insuranceCompanyReqDTO);
        return Result.success();
    }

    /**
     * 根据id逻辑删除保险公司
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteInsuranceCompanyById/{id}")
    public Result deleteInsuranceCompanyById(@PathVariable Long id) {
        if (id == null) {
            log.error("删除公司失败,失败原因:id为空");
            return Result.error("您删除的保司不存在");
        }
        insuranceCompanyService.deleteInsuranceCompanyById(id);
        return Result.success();
    }
}
