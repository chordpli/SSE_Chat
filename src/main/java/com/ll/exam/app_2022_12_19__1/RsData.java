package com.ll.exam.app_2022_12_19__1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RsData <T>{
    String resultCode;
    String msg;
    T data;
}
