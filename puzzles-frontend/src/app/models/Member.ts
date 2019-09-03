import {Puzzle} from './Puzzle';
import {Solution} from './Solution';
import {PuzzleComment} from './PuzzleComment';

export class Member {
  id: number;
  username: string;
  email: string;
  password: string;
  registration: Date;
  score: number;
  puzzles: Puzzle[];
  solutions: Solution[];
  comments: PuzzleComment[];
}
