package com.shoppinger.common.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,length = 256, nullable = false)
    private String name;
    @Column(unique = true,length = 256, nullable = false)
    private String alias;
    @Column(name = "short_description",length = 512, nullable = false)
    private String shortDescription;
    @Column(name = "full_description",length = 4096, nullable = false)
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;
    @Column(columnDefinition = "TINYINT(1)")
    private boolean enabled;
    @Column(name = "in_stock",columnDefinition = "TINYINT(1)")
    private boolean inStock;
    @Column( columnDefinition = "float default 0")
    private float cost;
    @Column( columnDefinition = "float default 0")
    private float price;
    @Column(name = "discount_percent" ,columnDefinition = "float default 0" )
    private float discountPercent;

    @Column( columnDefinition = "float default 0")
    private float length;
    @Column( columnDefinition = "float default 0")
    private float width;
    @Column( columnDefinition = "float default 0")
    private float height;
    @Column( columnDefinition = "float default 0")
    private float weight;
    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true )
    private Set<ProductImage> images = new HashSet<>();


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }
    public List<ProductDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductDetail> details) {
        this.details = details;
    }
    public void addExtraImage(String imageName){
        this.images.add(new ProductImage(imageName, this));
    }
    public void addProductDetails(String name, String value){
        this.details.add(new ProductDetail(name,value, this));
    }
    public void addProductDetails(Integer id,String name, String value){
        this.details.add(new ProductDetail(id,name,value, this));
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", enabled=" + enabled +
                ", inStock=" + inStock +
                ", cost=" + cost +
                ", price=" + price +
                ", discountPercent=" + discountPercent +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", category=" + category +
                ", brand=" + brand +
                '}';
    }

    @Transient
    public String getMainImagePath() {
        if(id == null || mainImage == null) return "/images/default-image.jpg";

        return "/product-images/" + this.id + "/" + this.mainImage;

    }

    public boolean containsImageName(String imageName) {
        Iterator<ProductImage> iterator = images.iterator();

        while (iterator.hasNext()) {
            ProductImage image = iterator.next();
            if(image.getName().equals(imageName)) {
                return true;
            }
        }
    return false;
    }

    @Transient
    public String getShortName(){
        if (name.length() > 70) {
            return name.substring(0,70).concat("..");
        }
        return name;
    }

    @Transient
    public float getDiscountPrice(){
        if (discountPercent > 0){
            return price * ((100 - discountPercent) / 100);
        }
        return this.price;
    }
}
