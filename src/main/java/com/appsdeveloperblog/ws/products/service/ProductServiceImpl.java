package com.appsdeveloperblog.ws.products.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Producer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.ws.core.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;


@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;
    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productRestModel) {
        String productID = UUID.randomUUID().toString();

        // TODO: Persistere il prodotto nel database prima di pubblicare l'evento;
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        productCreatedEvent.setProductId(productID);
        productCreatedEvent.setTitle(productRestModel.getTitle());
        productCreatedEvent.setPrice(productRestModel.getPrice());
        productCreatedEvent.setQuantity(productRestModel.getQuantity());

        // Pubblica l'evento di creazione del prodotto su Kafka in modo asincrono
       CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productID, productCreatedEvent)
           .completable();
        
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                // Gestisci l'errore di invio del messaggio
               log.error("Errore durante l'invio del messaggio: " + ex.getMessage());
            } else {
                // Messaggio inviato con successo
                log.error("Messaggio inviato con successo: " + result.getProducerRecord().value());
            }
        });

      //  future.join(); // Aspetta che il messaggio venga inviato prima di continuare
        log.info("Product created with ID: " + productID);
        return productID;
    }

    
    @Override
    public String createProduct2(CreateProductRestModel productRestModel) throws Exception {
        String productID = UUID.randomUUID().toString();

        // TODO: Persistere il prodotto nel database prima di pubblicare l'evento;
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        productCreatedEvent.setProductId(productID);
        productCreatedEvent.setTitle(productRestModel.getTitle());
        productCreatedEvent.setPrice(productRestModel.getPrice());
        productCreatedEvent.setQuantity(productRestModel.getQuantity());

        ProducerRecord<String, ProductCreatedEvent> producerRecord = new ProducerRecord<String,ProductCreatedEvent>("product-created-events-topic", productID, productCreatedEvent);        
        producerRecord.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        // Pubblica l'evento di creazione del prodotto su Kafka in modo sincrono
        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send(producerRecord)
            .get();
        


        
        log.info("Messaggio inviato con successo: " + result.getProducerRecord().value());
        log.info("Messaggio inviato con successo: " + result.getProducerRecord().key());
        // Gestisci l'errore di invio del messaggio
        log.info("Partution: " + result.getRecordMetadata().partition());
        log.info("Offset: " + result.getRecordMetadata().offset());
        if (result.getRecordMetadata().hasOffset()) {
            log.info("Messaggio inviato con successo con offset: " + result.getRecordMetadata().offset());
        } else {
            log.error("Errore durante l'invio del messaggio: Nessun offset disponibile");
        }
        log.info("Product created with ID: " + productID);
        return productID;
    }
}
