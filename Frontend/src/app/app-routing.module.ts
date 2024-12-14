import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TableComponent } from './Components/table/table.component';
import { UploadExcelComponent } from './Components/upload-excel/upload-excel.component';
import { InputManualComponent } from './Components/input-manual/input-manual.component';
import { RegisterComponent } from './Components/register/register.component';
import { LoginComponent } from './Components/login/login.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'table', component: TableComponent, canActivate:[AuthGuard] },
  { path: 'upload-excel', component:UploadExcelComponent, canActivate:[AuthGuard] },
  { path: 'input-manual', component:InputManualComponent,canActivate:[AuthGuard] },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
