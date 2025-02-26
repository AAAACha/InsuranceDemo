package com.schj.controller;

import cn.hutool.core.bean.BeanUtil;
import com.schj.pojo.dto.request.CustomerInfoReqDTO;
import com.schj.pojo.po.Result;
import com.schj.service.CustomerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/customer-info")
@Slf4j
public class CustomerInfoController {

    @Autowired
    CustomerInfoService customerInfoService;

    /**
     * 新增客户信息
     *
     * @param customerInfoReqDTO
     * @return
     */
    @PostMapping("/insertCustomerInfoService")
    public Result insertCustomerInfo(@RequestBody CustomerInfoReqDTO customerInfoReqDTO) {
        if (BeanUtil.isNotEmpty(customerInfoReqDTO)) {
            customerInfoService.insertCustomerInfoService(customerInfoReqDTO);
            return Result.success();
        } else {
            log.error("新增客户信息失败, 失败原因:请求体为空");
            return Result.error("新增客户信息失败,请检查录入的信息");
        }
    }

    /**
     * 根据id查询客户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getCustomerInfoById/{id}")
    public Result getCustomerInfoById(@PathVariable Long id) {
        if (BeanUtil.isNotEmpty(id)) {
            return Result.success(customerInfoService.getCustomerInfoById(id));
        } else {
            log.error("查询客户信息失败, 失败原因:id为空");
            return Result.error("您查询的客户信息不存在");
        }
    }

    /**
     * 根据id更新客户信息
     *
     * @param id
     * @param customerInfoReqDTO
     * @return
     */
    @PutMapping("/updateCustomerInfoById/{id}")
    public Result updateCustomerInfoById(@PathVariable Long id, @RequestBody CustomerInfoReqDTO customerInfoReqDTO) {
        if (BeanUtil.isNotEmpty(id)) {
            customerInfoService.updateCustomerInfoById(id, customerInfoReqDTO);
            return Result.success();
        } else {
            log.error("修改客户信息失败, 失败原因:请求体为空");
            return Result.error("修改失败,请检查您录入的客户信息");
        }
    }

    /**
     * 根据id删除客户信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCustomerInfoById/{id}")
    public Result deleteCustomerInfoById(@PathVariable Long id) {
        if (BeanUtil.isNotEmpty(id)) {
            customerInfoService.deleteCustomerInfoById(id);
            return Result.success();
        } else {
            log.error("删除客户信息失败, 失败原因:id为空");
            return Result.error("您删除的客户信息不存在");
        }
    }
}
