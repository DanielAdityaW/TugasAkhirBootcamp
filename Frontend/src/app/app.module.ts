import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TableComponent } from './Components/table/table.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TableService } from './Services/table.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { UploadExcelComponent } from './Components/upload-excel/upload-excel.component';
import { InputManualComponent } from './Components/input-manual/input-manual.component';
import { InputManualService } from './Services/input-manual.service';
import { FormsModule } from '@angular/forms';
import { PredictionHistoryComponent } from './Components/prediction-history/prediction-history.component';
import { PredictionHistoryService } from './Services/prediction-history.service';
import { RegisterComponent } from './Components/register/register.component';
import { AuthService } from './Services/auth.service';
import { LoginComponent } from './Components/login/login.component';
import { NavbarComponent } from './Components/navbar/navbar.component';


@NgModule({
  declarations: [
    AppComponent,
    TableComponent,
    UploadExcelComponent,
    InputManualComponent,
    PredictionHistoryComponent,
    RegisterComponent,
    LoginComponent,
    NavbarComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    FormsModule
],
  providers: [TableService, InputManualService, PredictionHistoryService, AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
