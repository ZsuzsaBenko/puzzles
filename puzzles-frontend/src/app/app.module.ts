import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { RatingModule } from 'ng-starrating';

import { AuthInterceptorService } from './services/auth-interceptor.service';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginComponent } from './components/welcome/login/login.component';
import { RegistrationComponent } from './components/welcome/registration/registration.component';
import { HomeComponent } from './components/home/home.component';
import { LeaderboardComponent } from './components/home/leaderboard/leaderboard.component';
import { RandomPuzzlesComponent } from './components/home/random-puzzles/random-puzzles.component';
import { PuzzlesComponent } from './components/puzzles/puzzles.component';
import { PuzzleGameComponent } from './components/puzzle-game/puzzle-game.component';
import { ProfileComponent } from './components/profile/profile.component';
import { MyDataComponent } from './components/profile/my-data/my-data.component';
import { MySolutionsComponent } from './components/profile/my-solutions/my-solutions.component';
import { MyPuzzlesComponent } from './components/profile/my-puzzles/my-puzzles.component';
import { MyCommentsComponent } from './components/profile/my-comments/my-comments.component';
import { CommentComponent } from './components/comment/comment.component';

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
    PuzzleGameComponent,
    ProfileComponent,
    MyDataComponent,
    MySolutionsComponent,
    MyPuzzlesComponent,
    MyCommentsComponent,
    CommentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RatingModule
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
