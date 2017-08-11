import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }    from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

//Plumbing
import { AppRoutingModule }     from './app-routing';

//Services
import { UserService } from './component/user/user.service';
import { DiaryService } from './component/diary/diary.service';

//Components
import { RootComponent } from './component/root/root';
import { UserLoginComponent } from './component/user/login';
import { DiaryDayComponent } from './component/diary/day';

@NgModule({
  declarations: [
    RootComponent,
    UserLoginComponent,
    DiaryDayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    UserService,
    DiaryService
  ],
  bootstrap: [RootComponent]
})
export class AppModule { }
