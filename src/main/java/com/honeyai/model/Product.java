package com.honeyai.model;

import com.honeyai.enums.HoneyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "honey_type")
    private HoneyType type;

    @NotBlank(message = "L'unité est obligatoire")
    @Column(nullable = false)
    private String unit;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Price> prices = new ArrayList<>();

    public void addPrice(Price price) {
        prices.add(price);
        price.setProduct(this);
    }

    public void removePrice(Price price) {
        prices.remove(price);
        price.setProduct(null);
    }
}
