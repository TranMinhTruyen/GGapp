package com.example.common.entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Tran Minh Truyen
 */

@Entity
@Data
public class Product implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column
	private String name;

	@Column
	private float price;

	@Column
	private String type;

	@Column
	private float discount;

	@Column(name = "in_stock")
	private float inStock;

	@ManyToOne
	@JoinColumn(name = "ID_BRAND")
	private Brand brand;

	@ManyToOne
	@JoinColumn(name = "ID_CATEGORY")
	private Category category;

	@Column(name = "day_created")
	private Date dayCreated;

	@Column(name = "is_new")
	private boolean isNew;
}
