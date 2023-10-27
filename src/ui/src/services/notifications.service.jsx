import axios from "axios";
import authHeader from "./auth-header";

const API_URL = "http://localhost:8080/api/admin/";

class notificationsService {

    getNotifications( action ) {
        
        const body = JSON.stringify(action)
        
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.post(API_URL +"seeNotifications", body, {headers})
    }

    async readNotification( id ) {
        const body = JSON.stringify(id)
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.post(API_URL +"notificationDetails", body, {headers})
    }

    async deleteAccount (account_id, notification_id) {
        return axios.delete(API_URL +"deleteAccount?userId="+account_id+"&noteId="+notification_id , {headers: authHeader()})

    }
    
    async deleteNotification(notification_id) {
        return axios.delete(API_URL + "notificationDelete?id="+notification_id, {headers: authHeader()});
    }
    
    async handleNotification(notification_id) {
        const body = JSON.stringify(notification_id);
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.put(API_URL + "notificationHandle", body,  {headers});
    }

    async changeFirstName(notification_id) {
        const body = JSON.stringify(notification_id);
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.put(API_URL + "changeFirstNameAccept", body, {headers});
    }

    async changeLastName(notification_id) {
        const body = JSON.stringify(notification_id);
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.put(API_URL + "changeLastNameAccept", body, {headers});
    }

    async acceptRequest(notification_id) {
        const body = JSON.stringify(notification_id);
        const headers = {"Content-type": "application/json"};
        headers["Authorization"] =  authHeader().Authorization;
        return axios.post(API_URL + "examAccessAccept", body, {headers});
    }
}
export default new notificationsService();