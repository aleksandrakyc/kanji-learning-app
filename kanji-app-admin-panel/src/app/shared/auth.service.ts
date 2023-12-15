import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private fireauth : AngularFireAuth, private router : Router) { }

  // login method
  login(email : string, password : string) {
    console.log(email, password)
    this.fireauth.signInWithEmailAndPassword(email,password).then( res => {
        localStorage.setItem('token','true');
        //check if user isAdmin
        this.userDetails()
    }, err => {
        alert(err.message);
        this.router.navigate(['/login']);
    })
  }

  userDetails() {
    this.fireauth.user.subscribe(
      (res) => console.info(res?.email)
      )
  }
}
