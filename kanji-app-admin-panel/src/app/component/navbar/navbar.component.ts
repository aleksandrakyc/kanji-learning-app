import { Component } from '@angular/core';
import { AuthService } from 'src/app/shared/auth.service';
import { DataService } from 'src/app/shared/data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  userEmail : string = ''
  loggedIn : boolean = false
  isAdmin : boolean = false

  constructor(private auth : AuthService, private dataService : DataService, private router : Router) { }

  ngOnInit(): void {
    this.auth.userDetails().subscribe(
      (res) => {
        if (res !== null && res.email !== null){
          this.userEmail = res.email    
          this.checkIfAdmin()
        }
        else {
          this.userEmail = ""  
          this.loggedIn = false
          this.router.navigate(['/login']);
        }
      }
      )
    console.log(this.userEmail, this.loggedIn)
  }

  checkIfAdmin(): void {
    this.dataService.getAdminByEmail(this.userEmail).then(
      (admin) => {
        try {
          this.isAdmin = admin[0].isAdmin
          if(this.isAdmin){
            this.loggedIn = true
          } 
          else{
            this.logout()
            this.router.navigate(['/login']);
            alert("This is not an administrator account!")
          }
        } catch (error) {
          this.logout()
          this.router.navigate(['/login']);
          alert("This is not an administrator account!")
        }

      }
    )
  }

  logout() {
    this.auth.logoutUser()
      .then(res => {
        console.log(res);
      })
      .catch(error => {
        console.log(error);
      })
  }
}
