const BASE_URL = "http://localhost:8080/api/employees";

export const API = {
  getEmployees: {
    url: BASE_URL,
  },
  findById: {
    url: BASE_URL + "/", // + id
  },
  addEmployee: {
    url: BASE_URL,
  },
  updateEmployee: {
    url: BASE_URL + "/", // + id
  },
  config: {
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
};
