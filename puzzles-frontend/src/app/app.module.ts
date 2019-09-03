import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginComponent } from './components/welcome/login/login.component';
import { RegistrationComponent } from './components/welcome/registration/registration.component';
import { HomeComponent } from './components/home/home.component';
import { LeaderboardComponent } from './components/home/leaderboard/leaderboard.component';
import { RandomPuzzlesComponent } from './components/home/random-puzzles/random-puzzles.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { PuzzlesComponent } from './components/puzzles/puzzles.component';
import { PuzzleItemComponent } from './components/puzzles/puzzle-item/puzzle-item.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    WelcomeComponent,
    LoginComponent,
    RegistrationComponent,
    HomeComponent,
    LeaderboardComponent,
    RandomPuzzlesComponent,
    PuzzlesComponent,
    PuzzleItemComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
