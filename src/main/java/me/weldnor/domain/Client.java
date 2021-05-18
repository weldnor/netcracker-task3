package me.weldnor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Client {
    private long id;
    private OperationType operationType;
    private int serviceTime;
    private int sum;
}
