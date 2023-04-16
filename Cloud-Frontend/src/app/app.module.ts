import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TweetappComponent } from './tweetapp/tweetapp.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule,HTTP_INTERCEPTORS } from "@angular/common/http";
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { NewtweetComponent } from './newtweet/newtweet.component';
import { TweetlistComponent } from './tweetlist/tweetlist.component';
import { ViewusersComponent } from './viewusers/viewusers.component';
import { ViewuserComponent } from './viewuser/viewuser.component';
import {JwtToken} from './models/jwtToken';
import {MainService} from './main.service';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    TweetappComponent,
    LoginComponent,
    RegisterComponent,
    NewtweetComponent,
    TweetlistComponent,
    ViewusersComponent,
    ViewuserComponent,
    ResetPasswordComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      { path: '', pathMatch: 'full', redirectTo: 'home' },
      { path: 'home', component: HomeComponent },
    ]),
    AppRoutingModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
   MainService,
   JwtToken,
   
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
