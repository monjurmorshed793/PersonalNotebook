import { IUser } from 'app/core/user/user.model';

export const enum MonthType {
    JANUARY = 'JANUARY',
    FEBRUARY = 'FEBRUARY',
    MARCH = 'MARCH',
    APRIL = 'APRIL',
    MAY = 'MAY',
    JUNE = 'JUNE',
    JULY = 'JULY',
    SEPTEMBER = 'SEPTEMBER',
    OCTOBER = 'OCTOBER',
    NOVEMBER = 'NOVEMBER',
    DECEMBER = 'DECEMBER'
}

export interface IMonthlyTask {
    id?: number;
    monthType?: MonthType;
    task?: string;
    description?: string;
    completed?: boolean;
    user?: IUser;
}

export class MonthlyTask implements IMonthlyTask {
    constructor(
        public id?: number,
        public monthType?: MonthType,
        public task?: string,
        public description?: string,
        public completed?: boolean,
        public user?: IUser
    ) {
        this.completed = this.completed || false;
    }
}
