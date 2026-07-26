package com.flashsale.flashsale_engine.config;

import com.flashsale.flashsale_engine.model.Sneaker;
import com.flashsale.flashsale_engine.repository.SneakerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.flashsale.flashsale_engine.service.RedisStockService;

@Configuration  // runs at startup as it contains configuration code
public class DataSeeder {

    @Bean
    public CommandLineRunner seedData(SneakerRepository sneakerRepository, RedisStockService redisStockService) { // commandlinerunner runs everything at startup (yaad agaya)
        return args -> {

            /*
            // Only seed if table is empty (prevents duplicate data on restart)
            if (sneakerRepository.count() > 0) {
                return;
            }
            */

            if (sneakerRepository.count() == 0) {
                LocalDateTime saleStart = LocalDateTime.now().minusMinutes(1);
                LocalDateTime saleEnd = saleStart.plusHours(72);

                sneakerRepository.save(Sneaker.builder()
                        .name("Air Jordan 1 Retro High OG")
                        .brand("Nike")
                        .price(new BigDecimal("14995"))
                        .totalStock(500)
                        .flashSaleStock(50)
                        .imageUrl("https://images.unsplash.com/photo-1693400652052-884f8dd3dfd9?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("Yeezy Boost 350 V2")
                        .brand("Adidas")
                        .price(new BigDecimal("22999"))
                        .totalStock(300)
                        .flashSaleStock(30)
                        .imageUrl("https://images.unsplash.com/photo-1723740240701-a9fbf7719fe8?q=80&w=910&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("Air Force 1 07")
                        .brand("Nike")
                        .price(new BigDecimal("7995"))
                        .totalStock(800)
                        .flashSaleStock(80)
                        .imageUrl("https://images.unsplash.com/photo-1656164753657-8ff832063a71?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("Ultraboost 22")
                        .brand("Adidas")
                        .price(new BigDecimal("16999"))
                        .totalStock(400)
                        .flashSaleStock(40)
                        .imageUrl("https://images.unsplash.com/photo-1580902394724-b08ff9ba7e8a?q=80&w=1074&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("RS-X")
                        .brand("Puma")
                        .price(new BigDecimal("8499"))
                        .totalStock(600)
                        .flashSaleStock(60)
                        .imageUrl("https://images.unsplash.com/photo-1715716234817-19e699050f60?q=80&w=1176&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("New Balance 550")
                        .brand("New Balance")
                        .price(new BigDecimal("9999"))
                        .totalStock(350)
                        .flashSaleStock(35)
                        .imageUrl("https://images.unsplash.com/photo-1628429437853-40ec8ebe3386?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("Chuck Taylor All Star")
                        .brand("Converse")
                        .price(new BigDecimal("4499"))
                        .totalStock(1000)
                        .flashSaleStock(100)
                        .imageUrl("https://images.unsplash.com/photo-1601131831144-5d096d7a832c?q=80&w=764&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("Old Skool")
                        .brand("Vans")
                        .price(new BigDecimal("5499"))
                        .totalStock(700)
                        .flashSaleStock(70)
                        .imageUrl("https://images.unsplash.com/photo-1618686081236-3ef081793aa6?q=80&w=1143&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                sneakerRepository.save(Sneaker.builder()
                        .name("Dunk Low Retro")
                        .brand("Nike")
                        .price(new BigDecimal("8995"))
                        .totalStock(450)
                        .flashSaleStock(45)
                        .imageUrl("https://images.unsplash.com/photo-1726200333701-df09434e48c4?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
                        .saleStartTime(saleStart)
                        .saleEndTime(saleEnd)
                        .build());

                System.out.println("9 sneakers seeded successfully!");
            }

            LocalDateTime freshSaleStart = LocalDateTime.now().minusMinutes(1);
            LocalDateTime freshSaleEnd = freshSaleStart.plusHours(72);

            sneakerRepository.findAll().forEach(sneaker -> {
                sneaker.setSaleStartTime(freshSaleStart);
                sneaker.setSaleEndTime(freshSaleEnd);
                sneakerRepository.save(sneaker);
            });
            System.out.println("Sale window refreshed for all sneakers!");

            sneakerRepository.findAll().forEach(sneaker -> redisStockService.initializeStock(sneaker.getId(), sneaker.getFlashSaleStock()));
            System.out.println("Redis stock initialized for all sneakers!");
        };
    }
}