package ro.tuc.ds2020.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.dtos.TimestampEnergyConsumptionDTO;

@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(TimestampEnergyConsumptionDTO fileBody) {
        System.out.println("Message " + fileBody.toString());
    }
}
