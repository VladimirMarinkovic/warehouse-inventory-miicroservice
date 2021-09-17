package com.example.warehouseinventorymicroservice.event;

import com.example.warehouseinventorymicroservice.model.InventoryItem;
import com.example.warehouseinventorymicroservice.model.Warehouse;
import com.example.warehouseinventorymicroservice.repository.InventoryItemRepository;
import com.example.warehouseinventorymicroservice.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
@Slf4j
@RequiredArgsConstructor
public class SampleDataInitializer {

    private final WarehouseRepository warehouseRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {

        var warehouse = warehouseRepository.save(Warehouse.builder().name("warehouse").build());

        Flux.just("Item1", "Item2", "Item3", "Item4", "Item5", "Item6", "Item7", "Item8", "Item9")
                .map(name -> InventoryItem.builder()
                                          .name(name)
                                          .type("item-type")
                                          .warehouse(warehouse)
                                          .build())
                .map(this.inventoryItemRepository::save)
                .subscribe(inventoryItem -> log.info("Inventory Item: {}", inventoryItem));

    }
}
