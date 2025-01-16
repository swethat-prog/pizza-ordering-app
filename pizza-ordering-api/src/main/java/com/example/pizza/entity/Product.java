package com.example.pizza.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
public class Product {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Lob
    private String description;

    private BigDecimal price;

    private Boolean available;
    
    
    
    public Product() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Product(UUID id, String name, String description, BigDecimal price, Boolean available) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.available = available;
	}

	public static class ProductBuilder {
        private String name;
        private String description;
        private BigDecimal price;
        private Boolean available;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder available(Boolean available) {
            this.available = available;
            return this;
        }

        public Product build() {
            return new Product(null, name, description, price, available);
        }
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }
}