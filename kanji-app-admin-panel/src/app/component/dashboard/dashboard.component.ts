import { Component, OnInit } from '@angular/core';
import { DataService } from 'src/app/shared/data.service';
import { Router } from '@angular/router';
import { Account } from 'src/app/models/account';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  constructor(private dataService : DataService, private router : Router) { }

  users : Account[] = []

  ngOnInit(): void {
    this.dataService.getAllAccounts().subscribe(
      res => {
        this.users = res.map((e: any) => {
          const data = e.payload.doc.data()
          console.log(data)
          data.id = e.payload.doc.id
          return data
        })
      } 
    )
  }

  deleteUser(id: string) {
    this.dataService.deleteUser(id)
  }

}
