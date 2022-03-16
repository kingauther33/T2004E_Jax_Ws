package com.example.backend.model;

import com.example.backend.entity.ShoppingCart;

import java.sql.SQLException;

public interface ShoppingCartModel {
    ShoppingCart get(int userId) throws SQLException; // 1 người dùng 1 shopping cart
    ShoppingCart save(ShoppingCart shoppingCart) throws SQLException;
    ShoppingCart update(int id, ShoppingCart updateObject) throws SQLException;
    boolean delete(int id) throws SQLException;
    boolean checkShoppingCartExisting(ShoppingCart shoppingCart) throws SQLException;
}
