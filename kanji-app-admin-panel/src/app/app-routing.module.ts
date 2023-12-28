import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './component/login/login.component';
import { TeachersComponent } from './component/teachers/teachers.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { RegisterComponent } from './component/register/register.component';
import { ChangePwdComponent } from './component/change-pwd/change-pwd.component';

const routes: Routes = [  
{path: 'login', component : LoginComponent},
{path: 'teachers', component : TeachersComponent},
{path: 'dashboard', component : DashboardComponent},
{path: 'register', component : RegisterComponent},
{path: 'change-pwd', component : ChangePwdComponent}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }