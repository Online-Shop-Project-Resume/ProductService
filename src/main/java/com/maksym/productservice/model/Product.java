package com.maksym.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "t_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name can't be blank")
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id")
    private ProductType type;
    @NotBlank(message = "Description can't be blank")
    private String description;
    @Positive(message = "Price has to be a positive number")
    private BigDecimal price;
    @Min(value = 0, message = "Rating must be grater or equal to 0")
    @Max(value = 5, message = "Rating must be less than or equal to 5")
    private Integer rating;
}
