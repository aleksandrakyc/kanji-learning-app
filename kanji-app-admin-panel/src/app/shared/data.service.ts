import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/compat/firestore';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private fireStore : AngularFirestore) { }

  //todo check if user isadmin
  getAdminByEmail(email: string) {
    return new Promise<any>((resolve)=> {
      this.fireStore.collection('/Users', ref => ref.where('email', '==', email).where('isAdmin', '==', true)).valueChanges().subscribe(product => resolve(product))
    })
  }

  //get all users
  getAllAccounts() {
    return this.fireStore.collection('/Users').snapshotChanges();
  }
  //todo get all users requesting teacher permissions

  //todo grant teacher permisions

  //todo do not grant teacher permissions ?

  //todo delete user
}
