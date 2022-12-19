package com.ll.exam.app_2022_12_19__1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsData <T>{
    private String resultCode;
    private String msg;
    private T data;
}
