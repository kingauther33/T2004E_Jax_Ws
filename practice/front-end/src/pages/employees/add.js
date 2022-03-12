import React, { useState } from "react";
import Head from "next/head";
import { useRouter } from "next/router";
import axios from "axios";
import * as Yup from "yup";
import { API } from "src/api";
import {
  Box,
  Button,
  Checkbox,
  Container,
  FormHelperText,
  Link,
  TextField,
  Typography,
} from "@mui/material";
import { useFormik } from "formik";
import { DashboardLayout } from "src/components/dashboard-layout";
import SnackbarPopup from "src/components/snackbar";
import NextLink from "next/link";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

const AddEmployee = () => {
  const router = useRouter();
  const [notification, setNotification] = useState({
    message: "",
    isSuccess: true,
    isOpen: false,
  });

  const formik = useFormik({
    initialValues: {
      name: "",
      salary: "",
    },
    validationSchema: Yup.object({
      name: Yup.string().max(255).required("Name is required"),
      salary: Yup.number()
        .typeError("Must be a number")
        .min(100, "Min salary is 100")
        .max(99999, "Max salary is 99999")
        .required("Salary is required"),
    }),
    onSubmit: async (values) => {
      console.log(values);
      const formData = {
        name: values.name,
        salary: values.salary,
      };
      await axios
        .post(API.addEmployee.url, formData, API.config)
        .then((res) => {
          setNotification({
            message: "Created Success",
            isSuccess: true,
            isOpen: true,
          });
        })
        .catch((err) => {
          console.error(err);
          setNotification({
            message: "Failed to create new employee! Restart server and try again!",
            isSuccess: false,
            isOpen: true,
          });
        });
    },
  });

  return (
    <>
      <Head>
        <title>Add</title>
      </Head>

      <Box
        component="main"
        sx={{
          alignItems: "center",
          display: "flex",
          flexGrow: 1,
          minHeight: "100%",
        }}
      >
        <Container maxWidth="sm">
          <NextLink href="/" passHref>
            <Button component="a" startIcon={<ArrowBackIcon fontSize="small" />}>
              Dashboard
            </Button>
          </NextLink>
          <form onSubmit={formik.handleSubmit}>
            <Box sx={{ my: 3 }}>
              <Typography color="textPrimary" variant="h4">
                Add new employee
              </Typography>
            </Box>
            <TextField
              error={Boolean(formik.touched.name && formik.errors.name)}
              fullWidth
              helperText={formik.touched.name && formik.errors.name}
              label="Name"
              margin="normal"
              name="name"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              value={formik.values.name}
              variant="outlined"
            />
            <TextField
              error={Boolean(formik.touched.salary && formik.errors.salary)}
              fullWidth
              helperText={formik.touched.salary && formik.errors.salary}
              label="Salary"
              margin="normal"
              name="salary"
              onBlur={formik.handleBlur}
              onChange={formik.handleChange}
              value={formik.values.salary}
              variant="outlined"
            />
            <Box sx={{ py: 2 }}>
              <Button
                color="primary"
                disabled={formik.isSubmitting}
                fullWidth
                size="large"
                type="submit"
                variant="contained"
              >
                Create
              </Button>
            </Box>
          </form>
        </Container>
      </Box>
      <SnackbarPopup notification={notification} setNotification={setNotification} />
    </>
  );
};

AddEmployee.getLayout = (page) => <DashboardLayout>{page}</DashboardLayout>;

export default AddEmployee;
