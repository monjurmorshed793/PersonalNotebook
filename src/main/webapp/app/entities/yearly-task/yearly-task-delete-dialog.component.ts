import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IYearlyTask } from 'app/shared/model/yearly-task.model';
import { YearlyTaskService } from './yearly-task.service';

@Component({
    selector: 'jhi-yearly-task-delete-dialog',
    templateUrl: './yearly-task-delete-dialog.component.html'
})
export class YearlyTaskDeleteDialogComponent {
    yearlyTask: IYearlyTask;

    constructor(private yearlyTaskService: YearlyTaskService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.yearlyTaskService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'yearlyTaskListModification',
                content: 'Deleted an yearlyTask'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-yearly-task-delete-popup',
    template: ''
})
export class YearlyTaskDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ yearlyTask }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(YearlyTaskDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.yearlyTask = yearlyTask;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
