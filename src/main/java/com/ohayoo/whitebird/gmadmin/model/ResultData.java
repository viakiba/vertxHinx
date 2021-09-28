package com.ohayoo.whitebird.gmadmin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author huangpeng.12@bytedance.com
 * @createTime 2021-04-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultData {

    private int code = 0;
    private String msg = "";
    private int count = 1000;
    private List<Object> data;

    public ResultData(List<Object> data) {
        this.data = data;
    }
}
