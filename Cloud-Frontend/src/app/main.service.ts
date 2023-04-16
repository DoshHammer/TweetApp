import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from "@angular/common/http";
import { LoginForm, RegisterForm, Reply, TweetForm,ResetForm } from "./models/userInputForm";
import { BehaviorSubject } from 'rxjs';
import { TweetEntity } from './models/incomingdata.model';
import {JwtToken} from './models/jwtToken';
@Injectable({
  providedIn: 'root'
})
export class MainService {
  
  //private PREFIX_PATH: string = "http://tweetapp.ap-south-1.elasticbeanstalk.com/api/v1.0/tweets/";
  private PREFIX_PATH: string = "http://localhost:5000/api/v1.0/tweets/";
  public selectedTweet: TweetEntity;

  public isUserLoggedIn: BehaviorSubject<boolean> = new BehaviorSubject(false);

  
  constructor(private http: HttpClient,
              private token:JwtToken) {

   }

 

   getSecurityToken(){
    // take result from AuthorizationMSClient service, using the security-token :: subscribe
    return this.token.Jwt;
    
  }
  setSecurityToken(token : string){
    // take result from AuthorizationMSClient service, using the security-token :: subscribe
    this.token.Jwt = token;
  }


  registerNewUser(data: RegisterForm) {
    return this.http.post(this.PREFIX_PATH+ "register", data,{responseType:'text'});
  }

  loginUser(data: LoginForm) {
    return this.http.post(this.PREFIX_PATH + "login",data, {responseType: 'json'});
  }

  resetUserPassword(username: string,data: ResetForm) {
    return this.http.post(this.PREFIX_PATH + username + "/forgot", data,
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token")),
      responseType: 'text'
      
    });
  }


  getAllTweets() {
    return this.http.get(this.PREFIX_PATH + "all",
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  getAllUsers() {
    return this.http.get(this.PREFIX_PATH + "users/all",
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  getUserTweets(username: string){
    return this.http.get(this.PREFIX_PATH + username);
  }

  searchByUsername(username: string) {
    return this.http.get(this.PREFIX_PATH + "user/search/" + username,
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  addTweetForUser(data: TweetForm) {
    return this.http.post(this.PREFIX_PATH + data.username+ "/add", data,
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  likeTweet(username: string, id: string) {
    return this.http.put(this.PREFIX_PATH + username + "/like/" + id, {},
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  replyTweet(data: Reply, id: string) {
    return this.http.post(this.PREFIX_PATH + data.username + "/reply/" + id, data,
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  updateTweet(data: TweetForm, id: string) {
    return this.http.put(this.PREFIX_PATH + data.username + "/update/" + id, data,
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });
  }

  deleteTweet(username: string, id: string) {
    return this.http.delete(this.PREFIX_PATH + username + "/delete/" + id,
    {
      headers: new HttpHeaders().set("Authorization", "Bearer "+localStorage.getItem("token"))
    });

  }

  getTweetById(id: string){
    return this.http.get(this.PREFIX_PATH + "find/" + id);
  }

  setProfile(params:any){
    return this.http.put(this.PREFIX_PATH + "avatar",params,{responseType:'text'})
  }

}
