package com.Long.JavaWiki.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResp<T> {

    private long total;

    private List<T> list;

}
