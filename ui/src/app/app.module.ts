import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

//Plumbing
import { AppRoutingModule }     from './app-routing';

//Services
import { UserService } from './component/user/user.service';

//Components
import { RootComponent } from './component/root/root';
import { UserLoginComponent } from './component/user/login';

@NgModule({
  declarations: [
    RootComponent,
    UserLoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    UserService,
  ],
  bootstrap: [RootComponent]
})
export class AppModule { }
