import { NgModule } from '@angular/core';

import { PersonalNotebookAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [PersonalNotebookAppSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [PersonalNotebookAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class PersonalNotebookAppSharedCommonModule {}
