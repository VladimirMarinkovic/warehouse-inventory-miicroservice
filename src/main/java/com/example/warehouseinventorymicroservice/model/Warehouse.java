package com.example.warehouseinventorymicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Table(name = "warehouse")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse implements Serializable {


    private static final long serialVersionUID = -5410035106382822063L;

    @Id
    @Column(name = "id", columnDefinition = "char(36)")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    private UUID id;


    @Column(name = "name", nullable = false)
    private String name;

}
