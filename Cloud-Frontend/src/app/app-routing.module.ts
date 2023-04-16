import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { NewtweetComponent } from './newtweet/newtweet.component';
import { RegisterComponent } from './register/register.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { TweetlistComponent } from './tweetlist/tweetlist.component';
import { ViewusersComponent } from './viewusers/viewusers.component';
import {AuthGuardService} from './auth-guard.service';
import { HomeComponent } from "./home/home.component";

const routes: Routes = [

  {
    path: "home",
    component: HomeComponent
  },
  
  {
    path: "login",
    component: LoginComponent
  },
  {
    path: "register",
    component: RegisterComponent
  },
  {
    path: "reset-password",
    component: ResetPasswordComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: "new",
    component: NewtweetComponent,
    canActivate: [AuthGuardService]
  }, 
  {
    path: "edit/:id",
    component: NewtweetComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: "userTweets/:id",
    component:TweetlistComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: "tweets",
    component: TweetlistComponent,
    canActivate: [AuthGuardService]
  },
  {
    path: "users",
    component: ViewusersComponent,
    canActivate: [AuthGuardService]
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
