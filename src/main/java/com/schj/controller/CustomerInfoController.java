package com.schj.controller;

import cn.hutool.core.bean.BeanUtil;
import com.schj.pojo.dto.request.CustomerInfoReqDTO;
import com.schj.pojo.dto.response.CustomerInfoResDTO;
import com.schj.pojo.po.Result;
import com.schj.service.CustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author AvA
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/customer-info")
public class CustomerInfoController {

    @Autowired
    CustomerInfoService customerInfoService;

    /**
     * 新增客户信息
     * @param customerInfoReqDTO
     * @return
     */
    @PostMapping("/insertCustomerInfoService")
    public Result insertCustomerInfo(@RequestBody CustomerInfoReqDTO customerInfoReqDTO) {
        if (BeanUtil.isNotEmpty(customerInfoReqDTO)) {
            Boolean result = customerInfoService.insertCustomerInfoService(customerInfoReqDTO);
            if (result) {
                return Result.success();
            } else {
                return Result.error("新增用户信息失败");
            }
        }
        return Result.error("用户信息不能为空");
    }

    /**
     * 根据id查询客户信息
     * @param id
     * @return
     */
    @GetMapping("/getCustomerInfoById/{id}")
    public Result getCustomerInfoById(@PathVariable Long id) {
        if (BeanUtil.isNotEmpty(id)) {
            CustomerInfoResDTO result = customerInfoService.getCustomerInfoById(id);
            if (BeanUtil.isNotEmpty(result)) {
                return Result.success(result);
            } else {
                return Result.error("查询用户信息失败");
            }
        } else {
            return Result.error("不能查询id为空的客户信息");
        }
    }

    /**
     * 根据id查询客户信息
     * @param id
     * @param customerInfoReqDTO
     * @return
     */
    @PutMapping("/updateCustomerInfoById/{id}")
    public Result updateCustomerInfoById(@PathVariable Long id, @RequestBody CustomerInfoReqDTO customerInfoReqDTO) {

        if (BeanUtil.isNotEmpty(id)) {
            if (BeanUtil.isNotEmpty(customerInfoService.getCustomerInfoById(id))) {
                Boolean result = customerInfoService.updateCustomerInfoById(id, customerInfoReqDTO);
                if (result) {
                    return Result.success();
                } else {
                    return Result.error("修改客户信息失败");
                }
            } else {
                return Result.error("要修改的客户id不存在");
            }
        } else {
            return Result.error("不能修改id为空的客户信息");
        }
    }

    /**
     * 根据id删除客户信息
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCustomerInfoById/{id}")
    public Result deleteCustomerInfoById(@PathVariable Long id){
        if (BeanUtil.isNotEmpty(id)) {
            if (BeanUtil.isNotEmpty(customerInfoService.getCustomerInfoById(id))) {
                Boolean result = customerInfoService.deleteCustomerInfoById(id);
                if (result) {
                    return Result.success();
                } else {
                    return Result.error("删除客户信息失败");
                }
            } else {
                return Result.error("要删除的客户id不存在");
            }
        } else {
            return Result.error("不能删除id为空的客户信息");
        }
    }
}
