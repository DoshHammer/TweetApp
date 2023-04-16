import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MainService } from '../main.service';
import { IncomingResponse } from '../models/incomingdata.model';
import { LoginForm } from '../models/userInputForm';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  state: string;
  submitted: boolean = false;
  error: boolean = false;
  errormsg: string;
  success: boolean = false;
  token:string=""
  successmsg: string;
  loginform: FormGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required)
  })
  constructor(private route: ActivatedRoute, private service: MainService, private currentRoute: Router) {
    
  }

  get f() { return this.loginform.controls; }

  ngOnInit(): void {
  }

  formSubmit() {

    this.submitted = true;
    let formData = new LoginForm();
    formData.username = this.f.username.value;
    formData.password = this.f.password.value;

    if (this.f.username.status== "VALID" && this.f.password.status == "VALID") {
      this.service.loginUser(formData).subscribe((res:IncomingResponse) => {
        if (res.msg) {
          this.success = true;
          this.successmsg = "User Logged In Successfully!"
          localStorage.setItem("tweetapp-loggeduser", formData.username);
          localStorage.setItem("token",res.token);
          //console.log(res.token)
          setTimeout(() => {
            this.service.isUserLoggedIn.next(true);
            this.currentRoute.navigate(['/tweets']);

          }, 1000)
        } else {
          this.error = true;
          this.errormsg = "Failed to Login! Invalid credentials";
        }

      },
      err=>{
        if(err.status == 400){
          this.error = true;
          this.errormsg = "Failed to Login! Invalid credentials"
        }
        else{
          console.log(err)
          this.error = true;
          this.errormsg = " Error connecting to Server! Please try later.";
        }
      });
    } 

  }

  resetForm() {
    this.submitted = false;
    this.error = false;
    this.success = false;
    this.errormsg = ""
  }

}
