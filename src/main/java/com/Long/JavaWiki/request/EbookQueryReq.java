package com.Long.JavaWiki.request;

import lombok.Data;

@Data
public class EbookQueryReq extends PageReq {

    private Long id;

    private String name;

    private Long categoryId2;
}
