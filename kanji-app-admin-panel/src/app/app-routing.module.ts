import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './component/login/login.component';
import { TeachersComponent } from './component/teachers/teachers.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';

const routes: Routes = [  
{path: 'login', component : LoginComponent},
{path: 'teachers', component : TeachersComponent},
{path: 'dashboard', component : DashboardComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }