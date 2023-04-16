import { Component, Input, OnInit,Output,EventEmitter } from '@angular/core';
import { MainService } from '../main.service';
import { RegisterForm } from '../models/userInputForm';

@Component({
  selector: 'app-viewuser',
  templateUrl: './viewuser.component.html',
  styleUrls: ['./viewuser.component.css']
})
export class ViewuserComponent implements OnInit {

  @Input("user")
  userData: RegisterForm;
  
  @Output("closeUser")
  closeCurrentUser: EventEmitter<boolean> = new EventEmitter();

  showDetails = false;

  user: RegisterForm;

  constructor(private service: MainService) {
  }

  ngOnInit(): void {
    this.service.searchByUsername(this.userData.username).subscribe((res: RegisterForm) => {
      
        this.user = res
        this.showDetails = true;      
    });
  }

  close(){
    //this.showDetails = false;
    this.closeCurrentUser.emit(true);
  }

}
