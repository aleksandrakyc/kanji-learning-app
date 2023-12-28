import { Component } from '@angular/core';
import { AuthService } from 'src/app/shared/auth.service';

@Component({
  selector: 'app-change-pwd',
  templateUrl: './change-pwd.component.html',
  styleUrls: ['./change-pwd.component.css']
})
export class ChangePwdComponent {

  newPassword : string = '';

  constructor(private auth : AuthService) { }

  ngOnInit(): void {
  }

  change() {
    if(this.newPassword == '') {
      alert('Please enter new password');
      return;
    }

    this.auth.changePwd(this.newPassword);

    this.newPassword = '';

  }
}