import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AuthGuardService } from './services/auth-guard.service';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { HomeComponent } from './components/home/home.component';
import { PuzzlesComponent } from './components/puzzles/puzzles.component';
import { PuzzleItemComponent } from './components/puzzles/puzzle-item/puzzle-item.component';

const routes: Routes = [
  {path: '', component: WelcomeComponent},
  {path: 'home', canActivate: [AuthGuardService], component: HomeComponent},
  {path: 'puzzles/riddles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/math-puzzles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/picture-puzzles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/word-puzzles', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/ciphers', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/all', canActivate: [AuthGuardService], component: PuzzlesComponent},
  {path: 'puzzles/all/:id', canActivate: [AuthGuardService], component: PuzzleItemComponent},
  {path: '**', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
