package me.weldnor.domain;

import lombok.*;
import lombok.extern.log4j.Log4j;

import java.util.LinkedList;
import java.util.Queue;


@Builder
@AllArgsConstructor
@Getter
@Setter
@Log4j
public class Operator extends Thread {
    private long id;

    @EqualsAndHashCode.Exclude
    private CashDesk cashDesk;

    @EqualsAndHashCode.Exclude
    private Queue<Client> clients = new LinkedList<>();

    public Operator(long id, CashDesk cashDesk) {
        this.id = id;
        this.cashDesk = cashDesk;
    }


    @Override
    public void run() {
        log.info("operator " + id + " started");

        while (!Thread.currentThread().isInterrupted()) {
            if (clients.isEmpty()) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            }

            var client = clients.poll();
            assert client != null;
            var clientId = client.getId();
            boolean operationIsSuccessful = cashDesk.tryMakeOperation(client.getSum(), client.getOperationType());

            try {
                //noinspection BusyWait
                Thread.sleep(client.getServiceTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (operationIsSuccessful) {
                log.info("operation by operator " + id + " client " + clientId + " completed successful");
            } else {
                log.info("operation by operator " + id + " client " + clientId + " completed unsuccessful :(");
            }
            log.info("current balance: " + cashDesk.getBalance());
        }

    }

    /**
     * @param client добавление клиента в очередь
     */
    public synchronized void addToQueue(Client client) {
        clients.add(client);
        notify();
    }

    /**
     * @return размер очереди
     */
    public int getQueueSize() {
        return clients.size();
    }
}
