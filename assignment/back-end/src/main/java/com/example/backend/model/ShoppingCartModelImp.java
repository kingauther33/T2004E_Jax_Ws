package com.example.backend.model;

import com.example.backend.entity.CartItem;
import com.example.backend.entity.ShoppingCart;
import com.example.backend.utils.ConnectionHelper;
import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingCartModelImp implements ShoppingCartModel {

    private Connection conn;

    public ShoppingCartModelImp() {
        conn = ConnectionHelper.getConnection();
    }

    @Override
    public ShoppingCart get(int userId) throws SQLException {
        conn.setAutoCommit(false);
        try {
            PreparedStatement stmtShoppingCart = conn.prepareStatement("select * from shopping_carts where userId = ?", Statement.RETURN_GENERATED_KEYS);
            stmtShoppingCart.setInt(1, userId);
            ResultSet resultSet = stmtShoppingCart.executeQuery();
            if (resultSet.next()) {
                ShoppingCart shoppingCart = new ShoppingCart();
                int shoppingCartId = resultSet.getInt("id");
                shoppingCart.setId(shoppingCartId);
                shoppingCart.setUserId(userId);
                shoppingCart.setShipName(resultSet.getString("shipName"));
                shoppingCart.setShipAddress(resultSet.getString("shipAddress"));
                shoppingCart.setShipPhone(resultSet.getString("shipPhone"));
                shoppingCart.setTotalPrice(resultSet.getDouble("totalPrice"));
                List<CartItem> cartItems = new ArrayList<>();
                try {
                    PreparedStatement stmtCartItems = conn.prepareStatement("select * from cart_items where shoppingCartId = ?", Statement.RETURN_GENERATED_KEYS);
                    stmtCartItems.setInt(1, shoppingCartId);
                    ResultSet resultSetCartItems = stmtCartItems.executeQuery();
                    while (resultSetCartItems.next()) {
                        CartItem cartItem = new CartItem();
                        cartItem.setShoppingCartId(shoppingCartId);
                        cartItem.setProductId(resultSetCartItems.getInt("productId"));
                        cartItem.setProductName(resultSetCartItems.getString("productName"));
                        cartItem.setUnitPrice(resultSetCartItems.getInt("unitPrice"));
                        cartItem.setQuantity(resultSetCartItems.getInt("quantity"));
                        cartItems.add(cartItem);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                shoppingCart.setCartItems(cartItems);
                return shoppingCart;
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        return null;
    }

    public ShoppingCart save(ShoppingCart shoppingCart) throws SQLException {
        conn.setAutoCommit(false);// begin transaction
        try {
            // trường hợp shopping cart null hoặc không có sản phẩm.
            if (shoppingCart == null || shoppingCart.getCartItems().size() == 0) {
                throw new Error("Shopping's null or empty.");
            }

            PreparedStatement stmtShoppingCart = conn.prepareStatement("insert into shopping_carts (userId, shipName, shipAddress, shipPhone, totalPrice) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            stmtShoppingCart.setInt(1, shoppingCart.getUserId());
            stmtShoppingCart.setString(2, shoppingCart.getShipName());
            stmtShoppingCart.setString(3, shoppingCart.getShipAddress());
            stmtShoppingCart.setString(4, shoppingCart.getShipPhone());
            stmtShoppingCart.setDouble(5, shoppingCart.getTotalPrice());
            int affectedRows = stmtShoppingCart.executeUpdate();
            if (affectedRows > 0) {
                ResultSet resultSetGeneratedKeys = stmtShoppingCart.getGeneratedKeys();
                if (resultSetGeneratedKeys.next()) {
                    int id = resultSetGeneratedKeys.getInt(1);
                    shoppingCart.setId(id);
                }
            }
            if (shoppingCart.getId() == 0) {
                throw new Error("Can't insert shopping cart.");
            }
            // insert cart items;
            for (CartItem item :
                    shoppingCart.getCartItems()) {
                PreparedStatement stmtCartItem = conn.prepareStatement("insert into cart_items (shoppingCartId, productId, productName, unitPrice, quantity) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmtCartItem.setInt(1, shoppingCart.getId());
                stmtCartItem.setInt(2, item.getProductId());
                stmtCartItem.setString(3, item.getProductName());
                stmtCartItem.setDouble(4, item.getUnitPrice());
                stmtCartItem.setInt(5, item.getQuantity());
                int affectedCartItemRows = stmtCartItem.executeUpdate();
                if (affectedCartItemRows == 0) { // lỗi
                    throw new Error("Insert cart item fails.");
                }
            }
            conn.commit(); // lưu tất cả vào db.
        } catch (Exception ex) {
            ex.printStackTrace();
            shoppingCart = null;
            conn.rollback();
        } finally {
            conn.setAutoCommit(true); // trả trạng thái auto commit default.
        }
        return shoppingCart;
    }

    public ShoppingCart update(int id, ShoppingCart updateObject) throws SQLException {
        conn.setAutoCommit(false);// begin transaction
        try {
            // trường hợp shopping cart null hoặc không có sản phẩm.
            if (updateObject == null || updateObject.getCartItems().size() == 0) {
                throw new Error("Shopping's null or empty.");
            }
            PreparedStatement stmtShoppingCart = conn.prepareStatement("update shopping_carts set shipName = ?, shipAddress = ?, shipPhone = ?, totalPrice = ? where id = ?", Statement.RETURN_GENERATED_KEYS);
            stmtShoppingCart.setString(1, updateObject.getShipName());
            stmtShoppingCart.setString(2, updateObject.getShipAddress());
            stmtShoppingCart.setString(3, updateObject.getShipPhone());
            stmtShoppingCart.setDouble(4, updateObject.getTotalPrice());
            stmtShoppingCart.setInt(5, id);
            int affectedRows = stmtShoppingCart.executeUpdate();
            if (affectedRows <= 0) {
                throw new Error("Can't update shopping cart.");
            }
            // delete old cart items.
            PreparedStatement stmtDeleteCartItem = conn.prepareStatement("delete from cart_items where shoppingCartId = ?", Statement.RETURN_GENERATED_KEYS);
            stmtDeleteCartItem.setInt(1, id);
            int affectedDeleteCartItemRows = stmtDeleteCartItem.executeUpdate();
            if (affectedDeleteCartItemRows == 0) { // lỗi
                throw new Error("Insert cart item fails.");
            }
            // update cart items;
            for (CartItem item :
                    updateObject.getCartItems()) {
                PreparedStatement stmtCartItem = conn.prepareStatement("insert into cart_items (shoppingCartId, productId, productName, unitPrice, quantity) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmtCartItem.setInt(1, id);
                stmtCartItem.setInt(2, item.getProductId());
                stmtCartItem.setString(3, item.getProductName());
                stmtCartItem.setDouble(4, item.getUnitPrice());
                stmtCartItem.setInt(5, item.getQuantity());
                int affectedCartItemRows = stmtCartItem.executeUpdate();
                if (affectedCartItemRows == 0) { // lỗi
                    throw new Error("Insert cart item fails.");
                }
            }
            conn.commit(); // lưu tất cả vào db.
        } catch (Exception ex) {
            updateObject = null;
            conn.rollback();
        } finally {
            conn.setAutoCommit(true); // trả trạng thái auto commit default.
        }
        return updateObject;
    }

    public boolean delete(int id) throws SQLException {
        conn.setAutoCommit(false);// begin transaction
        try {
            PreparedStatement stmtDeleteCartItem = conn.prepareStatement("delete from cart_items where shoppingCartId = ?");
            stmtDeleteCartItem.setInt(1, id);
            int affectedCartItemRows = stmtDeleteCartItem.executeUpdate();
            if (affectedCartItemRows <= 0) {
                return false;
            }
            PreparedStatement stmtDelete = conn.prepareStatement("delete from shopping_carts where id = ?");
            stmtDelete.setInt(1, id);
            int affectedRows = stmtDelete.executeUpdate();
            if (affectedRows <= 0) {
                return false;
            }
            conn.commit();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true); // trả trạng thái auto commit default.
        }
        return false;
    }

    public boolean checkShoppingCartExisting(ShoppingCart shoppingCart) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from shopping_carts where id = ?", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, shoppingCart.getId());
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
    }
}
