import Head from "next/head";
import { Box, Container } from "@mui/material";
import { DashboardLayout } from "src/components/dashboard-layout";
import { EmployeeListToolbar } from "src/components/employee/employee-list-toolbar";
import { EmployeeListResults } from "src/components/employee/employee-list-results";

const Employees = () => (
  <>
    <Head>
      <title>Employees</title>
    </Head>
    <Box
      component="main"
      sx={{
        flexGrow: 1,
        py: 8,
      }}
    >
      <Container maxWidth={false}>
        <EmployeeListToolbar />
        <Box sx={{ mt: 3 }}>
          <EmployeeListResults />
        </Box>
      </Container>
    </Box>
  </>
);
Employees.getLayout = (page) => <DashboardLayout>{page}</DashboardLayout>;

export default Employees;
