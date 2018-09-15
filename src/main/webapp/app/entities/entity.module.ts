import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { PersonalNotebookAppDailyTaskModule } from './daily-task/daily-task.module';
import { PersonalNotebookAppMonthlyTaskModule } from './monthly-task/monthly-task.module';
import { PersonalNotebookAppYearlyTaskModule } from './yearly-task/yearly-task.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        PersonalNotebookAppDailyTaskModule,
        PersonalNotebookAppMonthlyTaskModule,
        PersonalNotebookAppYearlyTaskModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PersonalNotebookAppEntityModule {}
