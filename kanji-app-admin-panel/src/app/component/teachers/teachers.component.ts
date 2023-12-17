import { Component, OnInit } from '@angular/core';
import { DataService } from 'src/app/shared/data.service';
import { Router } from '@angular/router';
import { Account } from 'src/app/models/account';
@Component({
  selector: 'app-teachers',
  templateUrl: './teachers.component.html',
  styleUrls: ['./teachers.component.css']
})
export class TeachersComponent  implements OnInit {
  constructor(private dataService : DataService, private router : Router) { }

  teacherRequests : Account[] = []

  ngOnInit(): void {
    this.dataService.getAllTeacherRequests().subscribe(
      res => {
        this.teacherRequests = res.map((e: any) => {
          const data = e.payload.doc.data()
          console.log(data)
          data.id = e.payload.doc.id
          return data
        })
      } 
    )
  }

  acceptClick(id: string): void {
    this.dataService.setTeacherPermissions(id, true)
  }

  rejectClick(id: string): void {
    this.dataService.setTeacherPermissions(id, false)
  }
}
