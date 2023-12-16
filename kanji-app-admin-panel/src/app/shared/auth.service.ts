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
        console.log("login successful for user " + res?.user?.email)
    }, err => {
        alert(err.message);
        this.router.navigate(['/login']);
    })
  }

  userDetails() {
    return this.fireauth.user
  }


  logoutUser() {
    return new Promise<void>(async (resolve, reject) => {
      if (await this.fireauth.currentUser) {
        this.fireauth.signOut()
          .then(() => {
            console.log("log out");
            resolve();
          }).catch((error) => {
            reject();
          });
      }
    })
  }
}
