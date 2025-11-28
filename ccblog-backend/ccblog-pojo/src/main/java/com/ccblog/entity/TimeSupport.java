package com.ccblog.entity;

import java.time.LocalDateTime;

public interface TimeSupport {
    void setCreateTime(LocalDateTime date);
    void setUpdateTime(LocalDateTime date);
}