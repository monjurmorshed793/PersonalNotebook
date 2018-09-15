import { IUser } from 'app/core/user/user.model';

export interface IYearlyTask {
    id?: number;
    year?: number;
    task?: string;
    description?: string;
    completed?: boolean;
    user?: IUser;
}

export class YearlyTask implements IYearlyTask {
    constructor(
        public id?: number,
        public year?: number,
        public task?: string,
        public description?: string,
        public completed?: boolean,
        public user?: IUser
    ) {
        this.completed = this.completed || false;
    }
}
