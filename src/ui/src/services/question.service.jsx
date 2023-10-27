import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/admin/";

class questionService {
    
    
    async searchQuestions( search ) {
        const body = JSON.stringify(search);
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.post(API_URL +"searchQuestion", body, {headers})
    }
    async deleteQuestion (id) {
        return axios.delete(API_URL +"deleteQuestion?id=" + id,  { headers: authHeader() })
    }
    async getQuestion(id){
        return axios.get(API_URL +"getQuestion?id=" +id, {headers : authHeader() });
    }
}
export default new questionService();