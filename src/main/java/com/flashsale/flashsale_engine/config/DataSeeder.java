package com.flashsale.flashsale_engine.config;

import com.flashsale.flashsale_engine.model.Sneaker;
import com.flashsale.flashsale_engine.repository.SneakerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
                // existing 10 sneaker saves stay exactly here, unchanged
                System.out.println("✅ 10 sneakers seeded successfully!");
            }

            sneakerRepository.findAll().forEach(sneaker -> redisStockService.initializeStock(sneaker.getId(), sneaker.getFlashSaleStock()));
            System.out.println("✅ Redis stock initialized for all sneakers!");
        };

            LocalDateTime saleStart = LocalDateTime.now().minusMinutes(1);
            LocalDateTime saleEnd = saleStart.plusHours(72);

            sneakerRepository.save(Sneaker.builder()
                    .name("Air Jordan 1 Retro High OG")
                    .brand("Nike")
                    .price(new BigDecimal("14995"))
                    .totalStock(500)
                    .flashSaleStock(50)
                    .imageUrl("https://source.unsplash.com/400x400/?nike,sneaker")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Yeezy Boost 350 V2")
                    .brand("Adidas")
                    .price(new BigDecimal("22999"))
                    .totalStock(300)
                    .flashSaleStock(30)
                    .imageUrl("https://source.unsplash.com/400x400/?yeezy,sneaker")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Air Force 1 07")
                    .brand("Nike")
                    .price(new BigDecimal("7995"))
                    .totalStock(800)
                    .flashSaleStock(80)
                    .imageUrl("https://source.unsplash.com/400x400/?airforce,sneaker")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Ultraboost 22")
                    .brand("Adidas")
                    .price(new BigDecimal("16999"))
                    .totalStock(400)
                    .flashSaleStock(40)
                    .imageUrl("https://source.unsplash.com/400x400/?adidas,running")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("RS-X")
                    .brand("Puma")
                    .price(new BigDecimal("8499"))
                    .totalStock(600)
                    .flashSaleStock(60)
                    .imageUrl("https://source.unsplash.com/400x400/?puma,sneaker")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("New Balance 550")
                    .brand("New Balance")
                    .price(new BigDecimal("9999"))
                    .totalStock(350)
                    .flashSaleStock(35)
                    .imageUrl("https://source.unsplash.com/400x400/?newbalance,sneaker")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Chuck Taylor All Star")
                    .brand("Converse")
                    .price(new BigDecimal("4499"))
                    .totalStock(1000)
                    .flashSaleStock(100)
                    .imageUrl("https://source.unsplash.com/400x400/?converse,sneaker")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Old Skool")
                    .brand("Vans")
                    .price(new BigDecimal("5499"))
                    .totalStock(700)
                    .flashSaleStock(70)
                    .imageUrl("https://source.unsplash.com/400x400/?vans,skateboard")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Dunk Low Retro")
                    .brand("Nike")
                    .price(new BigDecimal("8995"))
                    .totalStock(450)
                    .flashSaleStock(45)
                    .imageUrl("https://source.unsplash.com/400x400/?nike,dunk")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            sneakerRepository.save(Sneaker.builder()
                    .name("Classic Leather")
                    .brand("Reebok")
                    .price(new BigDecimal("6499"))
                    .totalStock(550)
                    .flashSaleStock(55)
                    .imageUrl("https://source.unsplash.com/400x400/?reebok,classic")
                    .saleStartTime(saleStart)
                    .saleEndTime(saleEnd)
                    .build());

            System.out.println("10 sneakers seeded successfully!");
        };
    }
}