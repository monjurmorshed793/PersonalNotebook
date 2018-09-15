import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PersonalNotebookAppSharedModule } from 'app/shared';
import { PersonalNotebookAppAdminModule } from 'app/admin/admin.module';
import {
    MonthlyTaskComponent,
    MonthlyTaskDetailComponent,
    MonthlyTaskUpdateComponent,
    MonthlyTaskDeletePopupComponent,
    MonthlyTaskDeleteDialogComponent,
    monthlyTaskRoute,
    monthlyTaskPopupRoute
} from './';

const ENTITY_STATES = [...monthlyTaskRoute, ...monthlyTaskPopupRoute];

@NgModule({
    imports: [PersonalNotebookAppSharedModule, PersonalNotebookAppAdminModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        MonthlyTaskComponent,
        MonthlyTaskDetailComponent,
        MonthlyTaskUpdateComponent,
        MonthlyTaskDeleteDialogComponent,
        MonthlyTaskDeletePopupComponent
    ],
    entryComponents: [MonthlyTaskComponent, MonthlyTaskUpdateComponent, MonthlyTaskDeleteDialogComponent, MonthlyTaskDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PersonalNotebookAppMonthlyTaskModule {}
