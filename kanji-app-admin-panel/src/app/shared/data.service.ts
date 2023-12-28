import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/compat/firestore';
import { Account } from '../models/account';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private fireStore : AngularFirestore) { }

  //check if user isadmin
  getAdminByEmail(email: string) {
    return new Promise<any>((resolve)=> {
      this.fireStore.collection('/Users', ref => ref.where('email', '==', email).where('isAdmin', '==', true)).valueChanges().subscribe(product => resolve(product))
    })
  }

  //check if any admin
  checkIfAdminExists() {
    return this.fireStore.collection('/Users', ref => ref.where('isAdmin', '==', true)).snapshotChanges()
  }

  //todo set admin
  createAdmin(admin : Account) {
    this.fireStore.collection('/Users').doc(admin.id).set(admin)
  }

  //get all users
  getAllAccounts() {
    return this.fireStore.collection('/Users').snapshotChanges();
  }

  //get all users requesting teacher permissions
  getAllTeacherRequests() {
    return this.fireStore.collection('/Users', ref => ref.where('teacherRequest', '==', true)).snapshotChanges()
  }

  //grant (or not) teacher permisions
  setTeacherPermissions(id: string, value : boolean) {
    this.fireStore.collection('/Users').doc(id).update({isTeacher : value, teacherRequest : false})
  }

  //delete user
  deleteUser(id: string){
    this.fireStore.collection('/Users').doc(id).delete()
    //todo delete from auth
  }
}
