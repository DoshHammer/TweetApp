import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { IncomingResponse } from '../models/incomingdata.model';

@Component({
  selector: 'app-viewusers',
  templateUrl: './viewusers.component.html',
  styleUrls: ['./viewusers.component.css']
})
export class ViewusersComponent implements OnInit {
  userNameList: any;
  userSelected: boolean = false;
  selectedUser: string;
  constructor(private service: MainService) {
    this.userNameList = [{}];
  }

  ngOnInit(): void {
    this.service.getAllUsers().subscribe((res) => {
        this.userNameList =res;
    });
  }

  selectUser(user: string) {
    // if (user != null && user == this.selectedUser && this.userSelected == true) {
    //   this.userSelected = false;
    // } else {
    //   this.userSelected = !this.userSelected;
    //   this.selectedUser = user;
    // }
    this.userSelected = true;
    this.selectedUser = user;
  }

  closeUser(){
    this.userSelected = false;
  }

}
