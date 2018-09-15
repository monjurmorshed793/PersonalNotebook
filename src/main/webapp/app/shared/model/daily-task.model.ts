import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';

export interface IDailyTask {
    id?: number;
    date?: Moment;
    task?: string;
    description?: string;
    completed?: boolean;
    user?: IUser;
}

export class DailyTask implements IDailyTask {
    constructor(
        public id?: number,
        public date?: Moment,
        public task?: string,
        public description?: string,
        public completed?: boolean,
        public user?: IUser
    ) {
        this.completed = this.completed || false;
    }
}
