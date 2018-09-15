/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { YearlyTaskDeleteDialogComponent } from 'app/entities/yearly-task/yearly-task-delete-dialog.component';
import { YearlyTaskService } from 'app/entities/yearly-task/yearly-task.service';

describe('Component Tests', () => {
    describe('YearlyTask Management Delete Component', () => {
        let comp: YearlyTaskDeleteDialogComponent;
        let fixture: ComponentFixture<YearlyTaskDeleteDialogComponent>;
        let service: YearlyTaskService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [YearlyTaskDeleteDialogComponent]
            })
                .overrideTemplate(YearlyTaskDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(YearlyTaskDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(YearlyTaskService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
