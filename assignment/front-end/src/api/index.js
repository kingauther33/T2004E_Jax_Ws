const BASE_URL = "http://localhost:8080/api/";

export const API = {
  getEmployees: {
    url: BASE_URL,
  },
  register: {
    url: BASE_URL + "accounts/register"
  },
  login: {
    url: BASE_URL + "accounts/login"
  },
  findById: {
    url: BASE_URL + "/",
  },
  addEmployee: {
    url: BASE_URL,
  },
  updateEmployee: {
    url: BASE_URL + "/",
  },
  config: {
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  },
};
