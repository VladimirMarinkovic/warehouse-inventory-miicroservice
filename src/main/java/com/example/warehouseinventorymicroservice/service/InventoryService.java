package com.example.warehouseinventorymicroservice.service;

import com.example.warehouseinventorymicroservice.dto.InventoryItemResponse;
import com.example.warehouseinventorymicroservice.dto.InventoryItemUpdateRequestDTO;
import com.example.warehouseinventorymicroservice.dto.InventoryItemsCountResponseDTO;
import com.example.warehouseinventorymicroservice.dto.InventoryPageResponseDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface InventoryService {

    @NotNull
    InventoryItemsCountResponseDTO getInventoryItemsCount(@NotBlank final String warehouseId);

    @NotNull
    InventoryPageResponseDTO getWarehouseInventoryItemsPaged(@NotBlank final String warehouseId, final Integer pageNumber, final Integer pageSize);

    @NotNull
    InventoryItemResponse getInventoryItem(@NotBlank final String warehouseId, @NotBlank final String itemId);

    @NotNull
    InventoryItemResponse updateInventoryItem(@NotBlank final String warehouseId, @NotBlank final String itemId, @NotNull InventoryItemUpdateRequestDTO updateRequest);
}
