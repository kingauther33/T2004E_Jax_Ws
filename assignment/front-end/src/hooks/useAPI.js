import { useState, useEffect } from "react";
import { useUser } from "src/providers/user-context";

const BASE_URL = "http://localhost:8080/api/v1/";

export const useAPI = () => {
  const { userInfo } = useUser();
  const [API, setAPI] = useState({
    config: {
      headers: {
        "Access-Control-Allow-Origin": "*",
        Authorization: userInfo.accessToken,
      },
    },
    register: {
      url: BASE_URL + "accounts/register",
    },
    login: {
      url: BASE_URL + "accounts/login",
    },
    getUserByToken: {
      url: BASE_URL + "accounts/getByToken",
    },
    getAccount: {
      url: BASE_URL + "accounts",
    },
    createTracsaction: {
      url: BASE_URL + "transactions",
    },
    getLogs: {
      url: BASE_URL + "logs",
    },
    checkApproveLoan: {
      url: BASE_URL + "loans/check_approve",
    },
    createLoan: {
      url: BASE_URL + "loans",
    },
    findAllLoan: {
      url: BASE_URL + "loans/find_all",
    },
    approveLoan: {
      url: BASE_URL + "loans/approve/", // + id
    },
  });

  useEffect(() => {
    setAPI((api) => ({
      ...api,
      config: {
        headers: {
          "Access-Control-Allow-Origin": "*",
          Authorization: userInfo.accessToken,
        },
      },
    }));
  }, [userInfo.accessToken]);

  return { API, setAPI };
};
