import axios from "axios";
import authHeader from "./auth-header";
import jwtDecode from "jwt-decode";


const API_URL = "http://localhost:8080/api/authentication/";
const CHANGEPASSWORD_URL = "http://localhost:8080/api/users/";

class AuthService {

  async login(email, password) {
    return await axios
      .post(
        API_URL + "authenticate",
        {
          email,
          password,
        },
        { headers: authHeader() }

      )
      .then((response) => {
        if (response.data.token) {
          this.fillSessionStorage(response.data);
        }
        return response.data;
      });
  }

  logout() {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("profile_picture");
    sessionStorage.removeItem("role");
    sessionStorage.removeItem("lastname");
    sessionStorage.removeItem("username");
  }

  register(userData) {
    const body = JSON.stringify(userData);
    const headers = { "Content-type": "application/json" };
    return axios.post(API_URL + "register", body, { headers });
  }

  resetPassword(resetEmail) {
    const body = JSON.stringify(resetEmail);
    const headers = { "Content-type": "application/json" };

    return axios.post(API_URL + "passwordReset", body, { headers });
  }

  changePassword(changePassword) {
    const body = JSON.stringify(changePassword);
    const headers = { "Content-type": "application/json" };
    headers["Authorization"] = authHeader().Authorization;
    return axios.put(CHANGEPASSWORD_URL + "changePassword", body, { headers})
  }

  resettingPassword(resetPassword, token) {
    const body = JSON.stringify(resetPassword);
    const headers = { "Content-type": "application/json" };
    return axios.put(API_URL + "resettingPassword?token=" + token, body, {
      headers,
    });
  }

  verifyEmail(token) {
    return axios.request(API_URL + "confirm?token=" + token);
  }
  resendEmail(token) {
    return axios.request(API_URL + "resendLink?token=" + token);
  }

  getCurrentUser() {
    const tokenString = sessionStorage.getItem("token");
    const userToken = JSON.parse(tokenString);
    return userToken?.userToken.user;
  }

  fillSessionStorage(token) {
    const decodedToken = jwtDecode(token.token);
    sessionStorage.setItem("token", JSON.stringify(token));
    sessionStorage.setItem("username", decodedToken.firstname);
    sessionStorage.setItem("lastname", decodedToken.lastname);
    sessionStorage.setItem("role", decodedToken.role); // This will contain the decoded data from the token  
  }
}
export default new AuthService();

