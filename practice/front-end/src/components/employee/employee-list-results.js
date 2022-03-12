import { useState, useEffect } from "react";
import PerfectScrollbar from "react-perfect-scrollbar";
import PropTypes from "prop-types";
import { format } from "date-fns";
import {
  Avatar,
  Button,
  Box,
  Card,
  Checkbox,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  Typography,
} from "@mui/material";
import axios from "axios";
import { API } from "src/api";
import SnackbarPopup from "src/components/snackbar";
import { useRouter } from "next/router";

export const EmployeeListResults = () => {
  const router = useRouter();
  const [employees, setEmployees] = useState([]);
  const [limit, setLimit] = useState(10);
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [notification, setNotification] = useState({
    message: "",
    isSuccess: true,
    isOpen: false,
  });

  const handleLimitChange = (event) => {
    setLimit(event.target.value);
  };

  const handlePageChange = (event, newPage) => {
    setPage(newPage);
  };

  useEffect(() => {
    const fetchEmployees = async () => {
      await axios
        .get(API.getEmployees.url, API.config)
        .then((res) => {
          setEmployees(res.data);
          console.log(res.data);
        })
        .catch((error) => {
          console.log(error);
          setNotification({
            message: "Please check restart the server and try again!",
            isSuccess: false,
            isOpen: true,
          });
        });
    };

    fetchEmployees();
    setIsLoading(false);
  }, []);

  return (
    <>
      {isLoading ? (
        "Loading ..."
      ) : (
        <Card>
          <PerfectScrollbar>
            <Box sx={{ minWidth: 1050 }}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Id</TableCell>
                    <TableCell>Name</TableCell>
                    <TableCell>Salary</TableCell>
                    <TableCell>Edit</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {employees.slice(0, limit).map((employee, index) => (
                    <TableRow hover key={employee.id}>
                      <TableCell>{employee.id}</TableCell>
                      <TableCell>{employee.name}</TableCell>
                      <TableCell>{employee.salary}</TableCell>
                      <TableCell>
                        <Button
                          onClick={() => router.push(`/employees/${employee.id}`)}
                          variant="contained"
                        >
                          Edit
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Box>
          </PerfectScrollbar>
          <TablePagination
            component="div"
            count={employees.length}
            onPageChange={handlePageChange}
            onRowsPerPageChange={handleLimitChange}
            page={page}
            rowsPerPage={limit}
            rowsPerPageOptions={[5, 10, 25]}
          />
        </Card>
      )}
      <SnackbarPopup notification={notification} setNotification={setNotification} />
    </>
  );
};
