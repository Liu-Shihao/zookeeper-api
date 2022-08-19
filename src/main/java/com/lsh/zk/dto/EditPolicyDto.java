package com.lsh.zk.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: LiuShihao
 * @Date: 2022/8/19 10:20
 * @Desc:
 */
@Data
public class EditPolicyDto {

    public String policyID;

    public String permission;

    public List<String> permissions;
}
