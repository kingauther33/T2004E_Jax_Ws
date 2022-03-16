export const addToLocalStorage = (data, setUserInfo, router) => {
  if (!data["access_token"]) {
    return;
  }

  localStorage.setItem("id", data["id"]);
  localStorage.setItem("access_token", data["access_token"]);
  localStorage.setItem("username", data["username"]);
  localStorage.setItem("firstName", data["first_name"]);
  localStorage.setItem("lastName", data["last_name"]);
  localStorage.setItem("email", data["email"]);
  localStorage.setItem("balance", data["balance"]);
  localStorage.setItem("role", data["role"]);
  setUserInfo({
    id: data["id"],
    accessToken: data["access_token"],
    username: data["username"],
    firstName: data["first_name"],
    lastName: data["last_name"],
    email: data["email"],
    balance: data["balance"],
    role: data["role"],
    isAdmin: data["role"] === "ADMIN",
  });

  router.replace("/");
};
