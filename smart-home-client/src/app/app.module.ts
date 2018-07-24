import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { RouterModule, Routes } from '@angular/router';
import { UserService } from './user.service';
import { RegisterComponent } from './register/register.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SettingsComponent } from './settings/settings.component';
import { AboutComponent } from './about/about.component';
import { SensorsComponent } from './sensors/sensors.component';
import { LogoutComponent } from './logout/logout.component';
import { AlkoholComponent } from './alkohol/alkohol.component';

const appRoutes:Routes = [
  {
    path: '',
    component: LoginFormComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'login',
    component: LoginFormComponent
  },
  {
    path: 'about',
    component: AboutComponent
  },
  {
    path: 'settings',
    component: SettingsComponent
  },
  {
    path: 'post',
    component: SensorsComponent
  },
  {
    path: 'logout',
    component: LogoutComponent
  },
  {
    path: 'alkohol',
    component: AlkoholComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LoginFormComponent,
    DashboardComponent,
    RegisterComponent,
    NavBarComponent,
    SettingsComponent,
    AboutComponent,
    SensorsComponent,
    LogoutComponent,
    AlkoholComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [UserService],
  bootstrap: [AppComponent]
})
export class AppModule { }
