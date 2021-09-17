package com.example.warehouseinventorymicroservice.service.impl;

import com.example.warehouseinventorymicroservice.dto.*;
import com.example.warehouseinventorymicroservice.model.InventoryItem;
import com.example.warehouseinventorymicroservice.repository.InventoryItemRepository;
import com.example.warehouseinventorymicroservice.repository.WarehouseRepository;
import com.example.warehouseinventorymicroservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.String.format;


@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final WarehouseRepository warehouseRepository;
    private final InventoryItemRepository inventoryItemRepository;


    @Override
    public @NotNull InventoryItemsCountResponseDTO getInventoryItemsCount(final String warehouseId) {
        return warehouseRepository.findById(UUID.fromString(warehouseId))
                                  .map(warehouse -> InventoryItemsCountResponseDTO.builder()
                                                                                  .warehouseId(warehouseId)
                                                                                  .totalElements((long) inventoryItemRepository.findAllByWarehouse(warehouse).size())
                                                                                  .build())
                                  .orElseThrow(() -> new EntityNotFoundException(format("There is not any warehouse with id %s.", warehouseId)));
    }


    @Override
    public @NotNull InventoryPageResponseDTO getWarehouseInventoryItemsPaged(final String warehouseId,
                                                                             final Integer pageNumber, final Integer pageSize) {
        final int determinedPage = max(1, pageNumber);
        log.debug("Get Inventory for warehouseId: {}", warehouseId);

        var warehouseInventoryItems = warehouseRepository.findById(UUID.fromString(warehouseId))
                .map(this.inventoryItemRepository::findAllByWarehouse)
                .map(inventoryItems -> inventoryItems.stream()
                                                     .map(this::mapInventoryItemToDto)
                                                     .collect(Collectors.toList()))
                .orElseThrow(() -> new EntityNotFoundException(format("There is not any warehouse with id %s.", warehouseId)));

        var inventoryItemsCount = warehouseInventoryItems.size();
        log.debug("Retrieving page {} with size {} and total items available: {}", determinedPage, pageSize, inventoryItemsCount);

        return InventoryPageResponseDTO.builder()
                                       .pageNumber(determinedPage)
                                       .pageSize(pageSize)
                                       .totalElements((long) warehouseInventoryItems.size())
                                       .totalPages(inventoryItemsCount == 0 ? 1 : (int) (Math.ceil(inventoryItemsCount / (float) pageSize)))
                                       .items(warehouseInventoryItems.stream()
                                                                     .skip((long) (pageNumber - 1) * pageSize)
                                                                     .limit(pageSize)
                                                                     .collect(Collectors.toList()))
                                       .build();
    }


    @Override
    public @NotNull InventoryItemResponse getInventoryItem(final String warehouseId, final String itemId) {
        return warehouseRepository.findById(UUID.fromString(warehouseId))
                                  .map(warehouse -> inventoryItemRepository.findById(UUID.fromString(itemId)))
                                  .filter(Optional::isPresent)
                                  .map(inventoryItem -> {
                                      var foundedInventoryItem = inventoryItem.get();
                                      return InventoryItemResponse.builder()
                                                                  .warehouseId(warehouseId)
                                                                  .itemId(foundedInventoryItem.getId().toString())
                                                                  .itemName(foundedInventoryItem.getName())
                                                                  .itemType(foundedInventoryItem.getType())
                                                                  .build();
                                  })
                                  .orElseThrow(() -> new EntityNotFoundException(format("There is no item with itemId: %s and warehouseId: %s .", itemId, warehouseId)));
    }

    @Override
    public @NotNull InventoryItemResponse updateInventoryItem(final String warehouseId,
                                                              final String itemId, final InventoryItemUpdateRequestDTO updateRequest) {

        return warehouseRepository.findById(UUID.fromString(warehouseId))
                                  .map(warehouse -> inventoryItemRepository.findById(UUID.fromString(itemId)))
                                  .filter(Optional::isPresent)
                                  .map(inventoryItem -> {
                                      var foundedInventoryItem = inventoryItem.get();
                                      foundedInventoryItem.setName(updateRequest.getName());
                                      foundedInventoryItem.setType(updateRequest.getType());
                                      var updatedItem =  inventoryItemRepository.save(foundedInventoryItem);
                                      return InventoryItemResponse.builder()
                                                                  .warehouseId(warehouseId)
                                                                  .itemId(updatedItem.getId().toString())
                                                                  .itemName(updatedItem.getName())
                                                                  .itemType(updatedItem.getType())
                                                                  .build();
                                  })
                                 .orElseThrow(() -> new EntityNotFoundException(format("There is no item with itemId: %s and warehouseId: %s .", itemId, warehouseId)));
    }


    private @NotNull InventoryPageItemDTO mapInventoryItemToDto(final InventoryItem inventoryItem) {
        return InventoryPageItemDTO.builder()
                                   .id(inventoryItem.getId().toString())
                                   .name(inventoryItem.getName())
                                   .type(inventoryItem.getType())
                                   .build();
    }

}
