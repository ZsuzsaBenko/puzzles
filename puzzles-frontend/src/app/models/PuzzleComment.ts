import {Member} from './Member';
import {Puzzle} from './Puzzle';

export class PuzzleComment {
  id: number;
  message: string;
  submissionTime: Date;
  puzzle: Puzzle;
  member: Member;
}
