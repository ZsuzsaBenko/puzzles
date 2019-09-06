import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthGuardService } from './services/auth-guard.service';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { HomeComponent } from './components/home/home.component';
import { PuzzlesComponent } from './components/puzzles/puzzles.component';
import { PuzzleGameComponent } from './components/puzzle-game/puzzle-game.component';
import { ProfileComponent } from './components/profile/profile.component';
import { CommentComponent } from './components/comment/comment.component';
import { AddPuzzleComponent } from './components/add-puzzle/add-puzzle.component';

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'home', canActivate: [AuthGuardService], component: HomeComponent},
  {path: 'puzzles/riddles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/math-puzzles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/picture-puzzles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/word-puzzles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/ciphers', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/all', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/add', canActivate: [AuthGuardService], component: AddPuzzleComponent, pathMatch: 'full'},
  {path: 'puzzles/:id', canActivate: [AuthGuardService], component: PuzzleGameComponent, pathMatch: 'full'},
  {path: 'puzzles/:id/comments', canActivate: [AuthGuardService], component: CommentComponent},
  {path: 'profile', canActivate: [AuthGuardService], component: ProfileComponent},
  {path: '**', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
