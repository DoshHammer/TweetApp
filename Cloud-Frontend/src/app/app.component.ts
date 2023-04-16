import { Component, OnChanges, SimpleChanges } from '@angular/core';
import { MainService } from './main.service';
import {Router } from '@angular/router';
import { FormBuilder, FormGroup } from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'tweetApp';
  isUserLoggedIn: boolean = false;
  loggedUsername: string;
  imageUrl:string
  image = false
  form: FormGroup;
  file: File;

  constructor(private service: MainService,private route:Router) {

    this.service.isUserLoggedIn.subscribe((res) => {
      this.isUserLoggedIn = res;
      if(localStorage.length > 0){
        let token = localStorage.getItem("token");
       
        this.isUserLoggedIn = token.includes(".");
      }
      if (this.isUserLoggedIn == true) {
        this.loggedUsername = localStorage.getItem("tweetapp-loggeduser");
        this.service.searchByUsername(this.loggedUsername).subscribe((res:any)=>{
          this.imageUrl = res.imageUrl;
        })
      }
    });
    
  }

  logOut(){
    if(localStorage.length>0){
    localStorage.removeItem("tweetapp-loggeduser");
    localStorage.removeItem("token")
    }
    this.service.isUserLoggedIn.next(false);
    this.handle()
  }

  handle(){
    this.service.isUserLoggedIn.next(false);
  }


  
  // fileChange(event: any) {
  //   if(event.target.files && event.target.files.length > 0) {
  //     this.file = event.target.files[0];
  //   }
  // }
  // upload() {
  //   let body = new FormData();
  //   body.append("username",this.loggedUsername);
  //   body.append("file", this.file);
  //   this.service.setProfile(body)
  //   .subscribe(
  //     (data) => {console.log(data)
  //       this.url = "http://localhost:8080/api/v1.0/tweets/avatar/"+data;
  //       this.image=true
  //     },
  //     error => console.log(error),
  //     () => { console.log("completed")
  //    }
  //   );
  // }

}
