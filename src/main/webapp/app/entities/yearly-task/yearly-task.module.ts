import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PersonalNotebookAppSharedModule } from 'app/shared';
import { PersonalNotebookAppAdminModule } from 'app/admin/admin.module';
import {
    YearlyTaskComponent,
    YearlyTaskDetailComponent,
    YearlyTaskUpdateComponent,
    YearlyTaskDeletePopupComponent,
    YearlyTaskDeleteDialogComponent,
    yearlyTaskRoute,
    yearlyTaskPopupRoute
} from './';

const ENTITY_STATES = [...yearlyTaskRoute, ...yearlyTaskPopupRoute];

@NgModule({
    imports: [PersonalNotebookAppSharedModule, PersonalNotebookAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        YearlyTaskComponent,
        YearlyTaskDetailComponent,
        YearlyTaskUpdateComponent,
        YearlyTaskDeleteDialogComponent,
        YearlyTaskDeletePopupComponent
    ],
    entryComponents: [YearlyTaskComponent, YearlyTaskUpdateComponent, YearlyTaskDeleteDialogComponent, YearlyTaskDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PersonalNotebookAppYearlyTaskModule {}
