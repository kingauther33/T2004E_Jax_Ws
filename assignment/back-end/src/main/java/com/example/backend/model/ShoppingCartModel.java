package com.example.backend.model;

import com.example.backend.entity.ShoppingCart;

import java.sql.SQLException;

public interface ShoppingCartModel {
    ShoppingCart get(int userId) throws SQLException; // 1 người dùng 1 shopping cart
    ShoppingCart create(int userId) throws SQLException; // 1 người dùng 1 shopping cart
    ShoppingCart save(ShoppingCart shoppingCart) throws SQLException;
    boolean remove(int id) throws SQLException;
    boolean clear(int id) throws SQLException;
    boolean checkShoppingCartExisting(ShoppingCart shoppingCart) throws SQLException;
}
