/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { PersonalNotebookAppTestModule } from '../../../test.module';
import { MonthlyTaskDeleteDialogComponent } from 'app/entities/monthly-task/monthly-task-delete-dialog.component';
import { MonthlyTaskService } from 'app/entities/monthly-task/monthly-task.service';

describe('Component Tests', () => {
    describe('MonthlyTask Management Delete Component', () => {
        let comp: MonthlyTaskDeleteDialogComponent;
        let fixture: ComponentFixture<MonthlyTaskDeleteDialogComponent>;
        let service: MonthlyTaskService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PersonalNotebookAppTestModule],
                declarations: [MonthlyTaskDeleteDialogComponent]
            })
                .overrideTemplate(MonthlyTaskDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(MonthlyTaskDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MonthlyTaskService);
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
