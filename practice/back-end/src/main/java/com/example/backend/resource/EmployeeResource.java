package com.example.backend.resource;

import com.example.backend.entity.Employee;
import com.example.backend.model.EmployeeModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("/employees")
public class EmployeeResource {

    private EmployeeModel employeeModel;

    public EmployeeResource() {
        this.employeeModel = new EmployeeModel();
    }

    @GET()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findById(@PathParam("id") int id) {
        try {
            return Response.status(Response.Status.OK).entity(employeeModel.findById(id)).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployees() {
        try {
            return Response.status(Response.Status.OK).entity(employeeModel.getEmployees()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.OK).entity(new ArrayList<>()).build();
        }
    }

    @POST()
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEmployee(Employee employee) {
        try {
            Employee savedProduct = employeeModel.addEmployee(employee);
            return Response.status(Response.Status.CREATED).entity(savedProduct).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @PUT()
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Employee employee) {
        try {
            Employee foundProduct = employeeModel.findById(id);
            if (foundProduct == null) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            Employee updatedProduct = employeeModel.updateEmployee(id, employee);
            return Response.status(Response.Status.OK).entity(updatedProduct).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
