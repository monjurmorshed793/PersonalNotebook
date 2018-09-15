import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PersonalNotebookAppSharedModule } from 'app/shared';
import { PersonalNotebookAppAdminModule } from 'app/admin/admin.module';
import {
    DailyTaskComponent,
    DailyTaskDetailComponent,
    DailyTaskUpdateComponent,
    DailyTaskDeletePopupComponent,
    DailyTaskDeleteDialogComponent,
    dailyTaskRoute,
    dailyTaskPopupRoute
} from './';

const ENTITY_STATES = [...dailyTaskRoute, ...dailyTaskPopupRoute];

@NgModule({
    imports: [PersonalNotebookAppSharedModule, PersonalNotebookAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        DailyTaskComponent,
        DailyTaskDetailComponent,
        DailyTaskUpdateComponent,
        DailyTaskDeleteDialogComponent,
        DailyTaskDeletePopupComponent
    ],
    entryComponents: [DailyTaskComponent, DailyTaskUpdateComponent, DailyTaskDeleteDialogComponent, DailyTaskDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PersonalNotebookAppDailyTaskModule {}
