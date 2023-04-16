import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MainService } from '../main.service';
import { ResetForm } from '../models/userInputForm';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  submitted: boolean = false;
  error: boolean = false;
  errormsg: string;
  success: boolean = false;
  successmsg: string;
  resetform: FormGroup = new FormGroup({
    password: new FormControl('', [Validators.required,Validators.minLength(6)]),
    confirmpassword: new FormControl('', Validators.required)
  },{ validators: this.checkPasswords })
  constructor(private service: MainService, private route: Router) {
    
  }

  checkPasswords(form: FormGroup) { // here we have the 'passwords' group
    const password = form.get('password').value;
    const confirmPassword = form.get('confirmpassword').value;
    return password === confirmPassword ? null : { notSame: true }
  }


  get f() { return this.resetform.controls; }
  get e() { return this.resetform.errors; }


  ngOnInit(): void {
  }

  formSubmit() {

    this.submitted = true;
    let formData = new ResetForm();
    formData.newPassword = this.f.password.value;
    formData.confirmNewPassword = this.f.confirmpassword.value;
    let username = localStorage.getItem('tweetapp-loggeduser');

    if (this.f.password.status== "VALID" && this.f.confirmpassword.status == "VALID") {
      this.service.resetUserPassword(username,formData).subscribe((res:any) => {

          if(res == "Password Changed Successfully"){
            this.successmsg = res;
            this.success = true;
            setTimeout(() => {
              // this.route.navigate['/login']
              // this.service.isUserLoggedIn.next(false);
              // localStorage.clear();
              this.logOut();
            }, 1200) 
          }
          else{
            this.errormsg = res;
            this.error = true;
          }
      },
      err=>{
          console.log(err);
          this.error = true;
          this.errormsg = " Error connecting to Server! Please try later.";
      });
    } 

  }

  logOut(){
    if(localStorage.length>0){
    localStorage.removeItem("tweetapp-loggeduser");
    localStorage.removeItem("token")
    }
    this.service.isUserLoggedIn.next(false);
    this.route.navigate(['/login'])
  }

  resetForm() {
    this.submitted = false;
    this.error = false;
    this.success = false;
    this.errormsg = ""
  }

}
