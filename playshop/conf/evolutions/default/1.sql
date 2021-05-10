# --- !Ups

create table "Category" (
    "id" integer primary key,
    "name" text not null
);

create table "PartsManufacturer" (
    "id" integer primary key,
    "name" text not null,
    "description" text
);

create table "Product" (
    "id" integer primary key,
    "name" text not null,
    "description" text,
    "price" integer not null,
    "partsManufacturerId" integer not null,
    "categoryId" integer not null,
    foreign key(partsManufacturerId) references PartsManufacturer(id),
    foreign key(categoryId) references Category(id)
);

create table "User" (
    "id" integer primary key,
    "username" text not null,
    "password" text not null
);

create table "Promotion" (
    "id" integer primary key,
    "percentage" real not null,
    "fromDate" text not null,
    "toDate" text not null,
    "productId" integer not null,
    foreign key(productId) references Product(id)
);

create table "CarMake" (
    "id" integer primary key,
    "name" text not null
);

create table "CarModel" (
    "id" integer primary key,
    "name" text not null,
    "year" integer not null,
    "carMakeId" integer not null,
    foreign key(carMakeId) references CarMake(id)
);

create table "Engine" (
   "id" integer primary key,
   "name" text not null,
   "carModelId" integer not null,
   foreign key(carModelId) references CarModel(id)
);

create table "ProductComment" (
    "id" integer primary key,
    "rate" real not null,
    "content" text,
    "userId" integer not null,
    "productId" integer not null,
    foreign key(userId) references User(id),
    foreign key(productId) references Product(id)
);

create table "UserOrder" (
    "id" integer primary key,
    "date" text not null,
    "userId" integer not null,
    "paymentId" integer not null,
    foreign key(userId) references User(id),
    foreign key(paymentId) references Payment(id)
);

create table "Payment" (
    "id" integer primary key,
    "provider" text not null,
    "amount" integer not null,
    "completed" integer not null
);

create table "OrderProduct" (
    "id" integer primary key,
    "quantity" integer not null,
    "orderId" integer not null,
    "productId" integer not null,
    foreign key(orderId) references UserOrder(id),
    foreign key(productId) references Product(id)
);

# --- !Downs
drop table "Category";
drop table "PartsManufacturer";
drop table "Product";
drop table "User";
drop table "Promotion";
drop table "CarMake";
drop table "CarModel";
drop table "Engine";
drop table "ProductComment";
drop table "Payment";
drop table "Order";
drop table "OrderProduct";