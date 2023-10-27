import axios from "axios";
import authHeader from "./auth-header";
import profilePictureValidation from "./profilePictureValidation";
import jwtDecode from "jwt-decode";

const USER_URL = "http://localhost:8080/api/users/";




class ChangeUserService {

  convertToBase64(file) {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        resolve(fileReader.result);
      };
      fileReader.onerror = (error) => {
        reject(error);
      };
    });
  };

  isImage(file) {
    if (file && file.type.startsWith("image/")) {
      return true;
    }
    return false;
  }

  changePassword(changePassword) {
    const body = JSON.stringify(changePassword);
    const headers = { "Content-type": "application/json" };
    headers["Authorization"] = authHeader().Authorization;

    return axios.put(USER_URL + "changePassword", body, { headers })
  }

  async changeName(name, field) {
    const body = JSON.stringify(name);
    const headers = { "Content-type": "application/json" };
    headers["Authorization"] = authHeader().Authorization;
    return (
      axios.put(USER_URL + "change" + field, body, { headers })
        .then((response) => {
          sessionStorage.setItem("token", (JSON.stringify({ token: response.data.token })));
          const decodedToken = jwtDecode(response.data.token);
          sessionStorage.setItem("username", decodedToken.firstname);
          
          sessionStorage.setItem("lastname", decodedToken.lastname);
          sessionStorage.setItem("role", decodedToken.role);
          return response;
        })
    )
  }

  async changePicture(
    event,
    setPictureError
  ) {
    
    const profile_picture = event.target.files[0];
    const formData = new FormData();
    const validation = new profilePictureValidation();
    if (validation.validateFile(profile_picture) === true) {
      const imgData = await this.convertToBase64(profile_picture);
      sessionStorage.setItem("profile_picture", imgData);
      formData.append("image", profile_picture)
      event.target.value = null;
      const body = profile_picture;
      const headers = { "Content-type": "image/png" };
      headers["Authorization"] = authHeader().Authorization;
      return axios.put(USER_URL + "addImage", formData, { headers }), imgData

    } else {
      setPictureError(validation.validateFile(profile_picture));
    }
  }
  async getProfilePicture() {
    return axios.get(
      USER_URL + "getImage",
      { headers: authHeader() })
      .then((response) => {
        if (response.data.base64image) {
          sessionStorage.setItem("profile_picture", "data:image/png;base64," + response.data.base64image);
        }
      });
  }

  async returnProfilePicture() {
    return axios.get(
      USER_URL + "getImage",
      { headers: authHeader() })
      .then((response) => {
        if (response.data.base64image) {
          return "data:image/png;base64,"+response.data.base64image;
        }
      });
  }

  async deleteImage() {
    const headers = { "Content-type": "image/png" };
    headers["Authorization"] = authHeader().Authorization;
    const body = null;
    return axios.put(
      USER_URL + "deleteImage", body,
      { headers }
    )
      .then((response) => {
        return response;
      })
  }

  async deleteUser(password) {
    const headers = { "Content-type": "application/json" };
    headers["Authorization"] = authHeader().Authorization;
    const body = JSON.stringify(password);
    return axios.put(USER_URL + "blockMyAccount", body, { headers })
      .then((response) => {
        return response;
      })
  }



};
export default new ChangeUserService();