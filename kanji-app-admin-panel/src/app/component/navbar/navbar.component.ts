import { Component } from '@angular/core';
import { AuthService } from 'src/app/shared/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  userEmail : string = ''
  loggedIn : boolean = false

  constructor(private auth : AuthService) { }

  ngOnInit(): void {
    this.auth.userDetails().subscribe(
      (res) => {
        console.log(res)
        if (res !== null && res.email !== null){
          this.userEmail = res.email    
          this.loggedIn = true
        }
        else {
          this.userEmail = ""  
          this.loggedIn = false
          console.log("help me")
        }
      }
      )
    console.log(this.userEmail, this.loggedIn)
  }

  getUserEmail(): void {

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
