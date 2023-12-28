import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/compat/auth';
import { Router } from '@angular/router';
import { DataService } from './data.service';
import { Account } from '../models/account';
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private fireauth : AngularFireAuth, private router : Router, private dataService : DataService) { }

  // login method
  login(email : string, password : string) {
    console.log(email, password)
    this.fireauth.signInWithEmailAndPassword(email,password).then( res => {
        console.log("login successful for user " + res?.user?.email)
        this.router.navigate(['/dashboard']);
    }, err => {
        alert(err.message);
        this.router.navigate(['/login']);
    })
  }

  register(email : string, password : string) {
    console.log(email, password)
    this.fireauth.createUserWithEmailAndPassword(email,password).then( res => {
        if (res.user !=null){
          this.dataService.createAdmin({
            id : res.user.uid,
            email : email,
            isTeacher : false,
            isAdmin : true})
          console.log("register successful for user " + res?.user?.email)
          this.router.navigate(['/dashboard']);
        }
    }, err => {
        alert(err.message);
        this.router.navigate(['/register']);
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

  changePwd(newP : string){
    
    try{
      this.fireauth.currentUser.then(user =>
        {
          user?.updatePassword(newP).then(
            res => console.log(res)
            
          )
        }
        ).then( res =>
          this.router.navigate(['/dashboard'])
        )
    }
    catch(e){
      alert("please log out and log in to change password")
    }

  }
}
