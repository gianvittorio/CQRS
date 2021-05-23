package com.gianvittorio.estore.ProductService.core.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "productlookup")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductLookupEntity implements Serializable {
    private static final long serialVersionUID = 914214829509504464L;

    @Id
    private String productId;

    @Column(unique = true)
    private String title;
}
