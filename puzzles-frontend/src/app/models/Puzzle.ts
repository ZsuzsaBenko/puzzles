import {Category} from './Category';
import {Level} from './Level';
import {Member} from './Member';
import {PuzzleComment} from './PuzzleComment';
import {Solution} from './Solution';

export class Puzzle {
  id: number;
  category: Category;
  level: Level;
  title: string;
  instruction: string;
  puzzleItem: string;
  answer: string;
  dateTime: Date;
  rating: number;
  member: Member;
  comments: PuzzleComment[];
  solutions: Solution[];
}
