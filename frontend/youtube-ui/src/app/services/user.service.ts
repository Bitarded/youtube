import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
 private userId: string = '';
  constructor(private httpClient:HttpClient) { }
  subscribeToUser(userId:string):Observable<boolean>{
   return  this.httpClient.post<boolean>("http://localhost:8080/user/subscribe/"+userId,null);

  }
  unsubscribeToUser(userId:string):Observable<boolean>{
    return  this.httpClient.post<boolean>("http://localhost:8080/user/unsubscribe/"+userId,null);

  }

  registerUser() {
     this.httpClient.get("http://localhost:8080/user/register",{responseType:"text"}).subscribe(data=>{
       this.userId = data;
     });
  }
  getUserId():string{
    return this.userId;
  }
}
