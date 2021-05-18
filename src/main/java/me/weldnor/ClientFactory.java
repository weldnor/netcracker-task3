package me.weldnor;

import me.weldnor.domain.Client;
import me.weldnor.domain.OperationType;

import java.util.List;
import java.util.Random;

public class ClientFactory {
    private static final int MIN_SUM = 1;
    private static final int MAX_SUM = 100;

    private static final int MIN_SERVICE_TIME = 5000;
    private static final int MAX_SERVICE_TIME = 10000;

    private final Random random = new Random();
    private long prevId = 0;

    public Client createClient() {
        return Client.builder()
                .id(createId())
                .operationType(createOperationType())
                .sum(createSum())
                .serviceTime(createServiceTime())
                .build();
    }

    private long createId() {
        prevId++;
        return prevId;
    }

    private OperationType createOperationType() {
        return getRandomElement(List.of(OperationType.GET_MONEY, OperationType.PUT_MONEY));
    }

    private int createSum() {
        return getRandomInt(MIN_SUM, MAX_SUM);
    }

    private int createServiceTime() {
        return getRandomInt(MIN_SERVICE_TIME, MAX_SERVICE_TIME);
    }

    private int getRandomInt(int start, int end) {
        return random.nextInt(end - start) + start;
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
