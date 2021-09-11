package com.Long.JavaWiki.request;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class PageReq {

    @NotNull(message = "[页码]不能为空")
    private int page;

    @NotNull(message = "[每页条数]不能为空")
    @Max(value = 100,message = "[每页条数]不能超过100")
    private int size;

}
