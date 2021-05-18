package me.weldnor;

import lombok.extern.log4j.Log4j;
import me.weldnor.domain.CashDesk;
import me.weldnor.domain.Client;
import me.weldnor.domain.Operator;

import java.util.ArrayList;
import java.util.List;

@Log4j
public class Game extends Thread {
    private static final int N = 5;
    private static final long CLIENTS_GENERATION_TIME = 1000;

    private final CashDesk cashDesk = new CashDesk(0);
    private final List<Operator> operators = new ArrayList<>();

    private final ClientFactory clientFactory = new ClientFactory();

    public void run() {
        log.info("start game");

        log.info("create " + N + " operators");
        for (int i = 0; i < N; i++) {
            operators.add(new Operator(i, cashDesk));
        }

        for (int i = 0; i < N; i++) {
            operators.get(i).start();
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Client client = clientFactory.createClient();
                log.info("create new client: " + client);

                int index = 0;
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < operators.size(); i++) {
                    int queueSize = operators.get(i).getQueueSize();
                    if (queueSize < min) {
                        min = queueSize;
                        index = i;
                    }
                }

                operators.get(index).addToQueue(client);
                log.info("add client with id: " + client.getId() + " to operator with id: " + index);
                //noinspection BusyWait
                Thread.sleep(CLIENTS_GENERATION_TIME);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
