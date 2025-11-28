package com.ccblog.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 操作单元
 * @author czc
 * @date 2025/11/9
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class OperationEvent {
    /**
     * 目标对象id
     */
    private Long targetId;

    /**
     * 操作代码
     */
    private Integer operationCode;
}