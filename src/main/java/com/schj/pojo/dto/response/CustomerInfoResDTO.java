package com.schj.pojo.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Descriptioin CustomerInfoResDTO
 * @Author AvA
 * @Date 2025-02-21
 */
@Data
public class CustomerInfoResDTO {
    private Long id;

    private String idType;

    private String idNumber;

    private String fullName;

    private String gender;

    private String contactPhone;

    private String contactAddress;

    private Integer benefitRatio;

    private LocalDateTime createdTime;

    private String creator;

    private LocalDateTime updatedTime;

    private String updater;

    private Integer isDeleted;
}
