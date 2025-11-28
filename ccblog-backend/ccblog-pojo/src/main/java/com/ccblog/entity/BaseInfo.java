package com.ccblog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor



public class BaseInfo implements Serializable,TimeSupport{
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
