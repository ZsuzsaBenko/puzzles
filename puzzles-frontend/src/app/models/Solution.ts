import {Member} from './Member';
import {Puzzle} from './Puzzle';

export class Solution {
  id: number;
  rating: number;
  seconds: number;
  submissionTime: Date;
  member: Member;
  puzzle: Puzzle;
}
