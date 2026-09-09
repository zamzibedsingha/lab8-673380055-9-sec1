# 🛒 Lab 8 — Product Shop (Spring Boot + PostgreSQL)

ระบบจัดการสินค้า (Product Shop) พัฒนาด้วย **Spring Boot**, **Spring Data JPA**, และ **PostgreSQL**
รองรับการจัดการสินค้า (Create / Read / Update / Delete) พร้อมรายละเอียดสินค้า รีวิว และระบบคำนวณส่วนลดด้วย **Strategy Pattern**

---

## 📌 Overview

โปรเจกต์นี้จำลองระบบร้านค้าออนไลน์ขนาดเล็ก โดยมี 3 ตารางหลักที่เชื่อมความสัมพันธ์กัน:

```
products  ──1:1──  product_details
products  ──1:N──  reviews
```

- **Product** — ข้อมูลหลักของสินค้า (ชื่อ, หมวดหมู่, ยี่ห้อ, ราคา, จำนวนคงคลัง)
- **ProductDetail** — รายละเอียดเพิ่มเติมของสินค้า (คำอธิบาย, การรับประกัน, น้ำหนัก, ขนาด)
- **Review** — รีวิวของสินค้า (ผู้รีวิว, คะแนน, ความคิดเห็น, วันที่รีวิว)

ระบบยังมีฟีเจอร์คำนวณราคาหลังหักส่วนลด โดยใช้ **Strategy Pattern** เพื่อรองรับส่วนลดหลายประเภท (สมาชิก, ตามฤดูกาล, ไม่มีส่วนลด) แบบที่สามารถเพิ่มประเภทใหม่ได้โดยไม่ต้องแก้โค้ดเดิม

---

## 🚀 Features

- ✅ CRUD สินค้าแบบเต็มรูปแบบ (Create, Read, Update, Delete)
- ✅ ความสัมพันธ์ 1:1 ระหว่าง Product และ ProductDetail
- ✅ ความสัมพันธ์ 1:N ระหว่าง Product และ Review
- ✅ คำนวณราคาหลังหักส่วนลดด้วย Strategy Pattern (Member / Seasonal / No Discount)
- ✅ ออกแบบตามหลัก SOLID Principles
- ✅ ใช้ Constructor Injection ตลอดทั้งโปรเจกต์

---

## 🛠️ Tech Stack

| ส่วนประกอบ         | เทคโนโลยี |
|--------------------|-----------|
| Backend Framework  | Spring Boot |
| ORM                | Spring Data JPA (Hibernate) |
| Database           | PostgreSQL |
| Template Engine    | Thymeleaf (HTML) |
| Build Tool         | Maven |
| DB Admin Tool      | pgAdmin |

---

## 📂 Project Structure

```
src/main/java/com/example/demo
├── model
│   ├── Product.java
│   ├── ProductDetail.java
│   └── Review.java
├── repository
│   ├── ProductRepository.java
│   ├── ProductDetailRepository.java
│   └── ReviewRepository.java
├── service
│   └── ProductService.java
├── controller
│   └── ProductController.java
└── strategy
    ├── DiscountStrategy.java
    ├── DiscountContext.java
    ├── NoDiscountStrategy.java
    ├── MemberDiscountStrategy.java
    └── SeasonalSaleStrategy.java

src/main/resources
├── templates/products
│   ├── list.html
│   ├── add.html
│   ├── edit.html
│   └── delete.html
└── application.properties
```

---

## 🗄️ Database Schema (ER Diagram)

```
products                        product_details
---------                       -----------------
id (PK)                         id (PK)
name                            description
category                        warranty
brand                           weight
stock                           dimensions
price                           manufactured_country
discount_type
detail_id (FK) ───────────────► id

products
---------
id (PK) ◄─────────────────────  product_id (FK)
                                 reviews
                                 ---------
                                 id (PK)
                                 reviewer
                                 rating
                                 comment
                                 review_date
                                 product_id
```

**ความสัมพันธ์:**

| ความสัมพันธ์ | ตาราง | Foreign Key |
|---|---|---|
| 1:1 | `products.detail_id` → `product_details.id` | `@OneToOne` + `@JoinColumn` |
| 1:N | `reviews.product_id` → `products.id` | `@ManyToOne` + `@JoinColumn` |

---

## 🧩 Design Principles (SOLID)

โปรเจกต์นี้ออกแบบตามหลัก SOLID เพื่อให้โค้ดแบ่งหน้าที่ชัดเจนและขยายต่อได้ง่าย

| หลักการ | การนำมาใช้ |
|---|---|
| **S** — Single Responsibility | แยก `Product`, `ProductDetail`, `Review`, `Service`, `Controller` ตามหน้าที่ของตัวเอง |
| **O** — Open/Closed | เพิ่มประเภทส่วนลดใหม่ได้โดยสร้าง class ใหม่ที่ implement `DiscountStrategy` โดยไม่ต้องแก้ `DiscountContext` |
| **L** — Liskov Substitution | ทุก Strategy (`MemberDiscountStrategy`, `SeasonalSaleStrategy`, `NoDiscountStrategy`) ใช้แทนกันได้ผ่าน `DiscountStrategy` |
| **I** — Interface Segregation | `DiscountStrategy` มีเฉพาะ method ที่เกี่ยวกับการคำนวณส่วนลดเท่านั้น |
| **D** — Dependency Inversion | `ProductService` และ `ProductController` รับ dependency ผ่าน Constructor แทนการสร้าง object เอง |

---

## 💰 Strategy Pattern — ระบบคำนวณส่วนลด

```
DiscountStrategy (interface)
       │
       ├── NoDiscountStrategy       → ไม่มีส่วนลด
       ├── MemberDiscountStrategy   → ลด 10%
       └── SeasonalSaleStrategy     → ลด 20%
```

`ProductService` จะเลือก Strategy ตามค่า `discountType` ของสินค้า แล้วส่งให้ `DiscountContext` เป็นตัวกลางในการคำนวณ โดยไม่แก้ไขค่า `price` เดิม แต่เก็บผลลัพธ์ไว้ใน field `discountedPrice` (`@Transient`) สำหรับแสดงผลเท่านั้น

---

## 🔄 Execution Flow

```
ผู้ใช้ (Browser)
     │  HTTP Request
     ▼
ProductController
     │
     ▼
ProductService  ──►  DiscountContext (คำนวณส่วนลด)
     │
     ▼
ProductRepository
     │
     ▼
Spring Data JPA / Hibernate
     │
     ▼
PostgreSQL (products, product_details, reviews)
```

---

## ⚙️ Setup & Run

### 1. Clone Repository

```bash
git clone <repository-url>
cd lab8shop
```

### 2. สร้างฐานข้อมูลใน PostgreSQL

```sql
CREATE DATABASE lab8shop;
```

### 3. ตั้งค่า `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lab8shop
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Build และ Run โปรเจกต์

```bash
./mvnw spring-boot:run
```

### 5. เปิดใช้งาน

เปิดเบราว์เซอร์ไปที่:

```
http://localhost:8080/products
```

---

## 🖥️ Endpoints

| Method | URL | หน้าที่ |
|--------|-----|---------|
| GET | `/products` | แสดงรายการสินค้าทั้งหมด |
| GET | `/products/add` | เปิดหน้าเพิ่มสินค้า |
| POST | `/products/save` | บันทึกสินค้าใหม่ |
| GET | `/products/edit/{id}` | เปิดหน้าแก้ไขสินค้า |
| POST | `/products/update/{id}` | อัปเดตข้อมูลสินค้า |
| GET | `/products/delete/{id}` | เปิดหน้ายืนยันการลบ |
| POST | `/products/delete/{id}` | ลบสินค้า |

---

## 📸 Screenshots

> เพิ่มภาพหน้าจอของระบบที่นี่ (Create / Read / Update / Delete / Database)

| Create | Read | Update | Delete |
|--------|------|--------|--------|
| _screenshot_ | _screenshot_ | _screenshot_ | _screenshot_ |

---

## 👤 Author

จัดทำโดย รติมา สวัสดิ์นที 673380055-9 — Lab 8 : Spring Boot + JPA Relationships & Design Patterns
