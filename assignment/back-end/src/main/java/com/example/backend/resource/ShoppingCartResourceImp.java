package com.example.backend.resource;

import com.example.backend.entity.Product;
import com.example.backend.entity.ShoppingCart;
import com.example.backend.model.ProductModel;
import com.example.backend.model.ShoppingCartModel;
import com.example.backend.model.ShoppingCartModelImp;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/carts")
public class ShoppingCartResourceImp implements ShoppingCartResource {

    private ShoppingCartModel shoppingCartModel;
    private ProductModel productModel;

    public ShoppingCartResourceImp() {
        this.shoppingCartModel = new ShoppingCartModelImp();
        this.productModel = new ProductModel();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@HeaderParam("Authorization") int userId) {
        try {
            ShoppingCart shoppingCart = this.shoppingCartModel.get(userId);
            return Response.status(Response.Status.OK).entity(shoppingCart).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ShoppingCart()).build();
        }
    }

    @GET
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(@HeaderParam("Authorization") int userId, @QueryParam("productId") int productId, @QueryParam("quantity") int quantity) {
        // kiểm tra số lượng
        if (quantity <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        // kiểm tra sản phẩm.
        Product product = null;
        try {
            product = this.productModel.findById(productId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (product == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        ShoppingCart shoppingCart = null;
        try {
            // check shopping cart trong db theo id người dùng.
            shoppingCart = this.shoppingCartModel.get(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            // trường hợp không có thì tạo mới.
        }
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
        }
        // do something.
        shoppingCart.add(product, quantity);
        try {
            boolean isShoppingCartItemExisting = this.shoppingCartModel.checkShoppingCartExisting(shoppingCart);
            if (!isShoppingCartItemExisting) {
                shoppingCart = this.shoppingCartModel.save(shoppingCart);
            } else {
                shoppingCart = this.shoppingCartModel.update(shoppingCart.getId(), shoppingCart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            shoppingCart = null;
        }
        if (shoppingCart == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ShoppingCart()).build();
        }
        return Response.status(Response.Status.CREATED).entity(shoppingCart).build();
    }

    @GET
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(@HeaderParam("Authorization") int userId, @QueryParam("productId") int productId, @QueryParam("quantity") int quantity) {
        // kiểm tra số lượng
        if (quantity <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // kiểm tra sản phẩm.
        Product product = null;
        try {
            product = this.productModel.findById(productId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (product == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ShoppingCart shoppingCart = null;
        try {
            // check shopping cart trong db theo id người dùng.
            shoppingCart = this.shoppingCartModel.get(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            // trường hợp không có thì tạo mới.
        }

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(userId);
        }

        shoppingCart.update(product, quantity);

        try {
            boolean isShoppingCartItemExisting = this.shoppingCartModel.checkShoppingCartExisting(shoppingCart);
            if (!isShoppingCartItemExisting) {
                shoppingCart = this.shoppingCartModel.save(shoppingCart);
            } else {
                shoppingCart = this.shoppingCartModel.update(shoppingCart.getId(), shoppingCart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            shoppingCart = null;
        }
        if (shoppingCart == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ShoppingCart()).build();
        }
        return Response.status(Response.Status.CREATED).entity(shoppingCart).build();
    }

    @GET
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response remove(@HeaderParam("Authorization") int userId, @QueryParam("productId") int productId) {
        // kiểm tra sản phẩm.
        Product product = null;
        try {
            product = this.productModel.findById(productId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (product == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        ShoppingCart shoppingCart = null;
        try {
            // check shopping cart trong db theo id người dùng.
            shoppingCart = this.shoppingCartModel.get(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            // trường hợp không có thì tạo mới.
        }

        if (shoppingCart == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ShoppingCart()).build();
        }

        shoppingCart.remove(product);

        boolean isDeletedSuccess = false;

        try {
            isDeletedSuccess = this.shoppingCartModel.delete(shoppingCart.getId());
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(isDeletedSuccess).build();
        }

        return Response.status(Response.Status.CREATED).entity(isDeletedSuccess).build();
    }

    @GET
    @Path("/clear")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response clear(@HeaderParam("Authorization") int userId) {
        return null;
    }
}

