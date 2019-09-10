import {Category} from './Category';
import {Level} from './Level';
import {Member} from './Member';

export class Puzzle {
  id: number;
  category: Category;
  level: Level;
  title: string;
  instruction: string;
  puzzleItem: string;
  answer: string;
  submissionTime: Date;
  rating: number;
  member: Member;
  solved: boolean;
}
