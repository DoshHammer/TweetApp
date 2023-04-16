import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MainService } from "../main.service";
import { RegisterForm } from "../models/userInputForm";
import { Router } from '@angular/router';
import { IncomingResponse } from "../models/incomingdata.model";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  submitted: boolean = false;
  userExists: boolean = false;
  registerFormData: RegisterForm;
  success: boolean = false;
  error: boolean = false;
  registerForm: FormGroup = new FormGroup({
    username: new FormControl('', Validators.required),
    firstName: new FormControl('', Validators.required),
    lastName: new FormControl('', Validators.required),
    email: new FormControl('', [Validators.required, Validators.email]),
    contactNumber: new FormControl('', [Validators.required,Validators.pattern(new RegExp("[0-9 ]{10}"))]),
    password: new FormControl('', [Validators.required,Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required])
  }, { validators: this.checkPasswords });

  checkPasswords(form: FormGroup) { // here we have the 'passwords' group
    const password = form.get('password').value;
    const confirmPassword = form.get('confirmPassword').value;
    return password === confirmPassword ? null : { notSame: true }
  }


  get f() { return this.registerForm.controls; }

  get e() { return this.registerForm.errors; }



  constructor(private service: MainService,private route:Router) {
    this.registerFormData = new RegisterForm();
  }

  ngOnInit(): void {
  }

  formSubmit(formData) {
    this.submitted = true;
    if (formData.status == "VALID") {
      this.registerFormData.username = this.f.username.value;
      this.registerFormData.password = this.f.password.value;
      this.registerFormData.firstName = this.f.firstName.value;
      this.registerFormData.lastName = this.f.lastName.value;
      this.registerFormData.contactNumber = this.f.contactNumber.value;
      this.registerFormData.email = this.f.email.value;
     // console.log(this.registerFormData)
      this.service.registerNewUser(this.registerFormData).subscribe((res) => {
        if (res == "User registered successfully!") {
          this.registerForm.reset();
          this.submitted = false;
          this.userExists = false;
          this.success = true;
          setTimeout(() => {
            this.route.navigate(['/login']);
          }, 1000)
        }
      },
      err=>{
            if(err.status==400)
             this.userExists=true;
            else
              this.error=true;
              console.log(err)
      }
      );
    }


  }

  resetMessage() {
    this.userExists = false;
    this.success = false;
    this.error = false;
  }

}
