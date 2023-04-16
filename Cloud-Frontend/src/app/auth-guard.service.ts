import { Injectable } from '@angular/core';
import {CanActivate, Router, RouterStateSnapshot, ActivatedRouteSnapshot} from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {
  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    let token = localStorage.length>0?localStorage.getItem("token"):"";
    if(token.includes(".")){
      return true;
    }
    else {
      alert('Please log in')
      this.router.navigate(['/login']);
      return false;
    }
  }
}
