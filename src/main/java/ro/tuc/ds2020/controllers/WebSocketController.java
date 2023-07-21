package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.services.RabbitMQProducer;

@RestController
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template1;

    @Autowired
    private SimpMessagingTemplate template2;

    @Autowired
    private RabbitMQProducer queueSender1;

    @Autowired
    private RabbitMQProducer queueSender2;

    @Scheduled(cron = "0/10 * * * * ?")
    private void sendNotification1(){
        template1.convertAndSend("/topic/notification", queueSender1.send1());
    }

    //read from csv file every 10 seconds
    @Scheduled(cron = "0/10 * * * * ?")
    private void sendNotification2(){
        template2.convertAndSend("/topic/notification", queueSender2.send2());
    }

    //read from csv file every 10 minutes
  /*  @Scheduled(cron = "0 0/10 * * * ?")
    private void sendNotification1(){
        template.convertAndSend("/topic/notification", queueSender1.send1());
    }*/
}
