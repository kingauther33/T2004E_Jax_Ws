package com.example.backend.model;

import com.example.backend.entity.Employee;
import com.example.backend.utils.ConnectionHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    private Connection conn;

    public EmployeeModel() {
        conn = ConnectionHelper.getConnection();
    }

    public List<Employee> getEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("select * from employees");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double salary = rs.getDouble("salary");
            employees.add(new Employee(id, name, salary));
        }

        return employees;
    }

    public Employee findById(int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from employees where id = ?");
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            double salary = rs.getInt("salary");
            return new Employee(id, name, salary);
        }
        return null;
    }

    public Employee addEmployee(Employee employee) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("insert into employees (name, salary) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, employee.getName());
        stmt.setDouble(2, employee.getSalary());
        int affectedRows = stmt.executeUpdate();
        if (affectedRows > 0) {
            // chú ý tạo tables trong db cần có contraint IDENTITY ở column id
            ResultSet resultSetGeneratedKeys = stmt.getGeneratedKeys();
            if (resultSetGeneratedKeys.next()) {
                int id = resultSetGeneratedKeys.getInt(1);
                employee.setId(id);
                return employee;
            }
        }
        return null;
    }

    public Employee updateEmployee(int id, Employee updateEmployee) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("update employees set name = ?, salary = ? where id = ?");
        stmt.setString(1, updateEmployee.getName());
        stmt.setDouble(2, updateEmployee.getSalary());
        stmt.setInt(3, id);
        int affectedRows = stmt.executeUpdate();
        if (affectedRows > 0) {
            updateEmployee.setId(id);
            return updateEmployee;
        }
        return null;
    }
}
