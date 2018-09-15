import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MonthlyTaskComponentsPage, MonthlyTaskUpdatePage } from './monthly-task.page-object';

describe('MonthlyTask e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let monthlyTaskUpdatePage: MonthlyTaskUpdatePage;
    let monthlyTaskComponentsPage: MonthlyTaskComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load MonthlyTasks', async () => {
        await navBarPage.goToEntity('monthly-task');
        monthlyTaskComponentsPage = new MonthlyTaskComponentsPage();
        expect(await monthlyTaskComponentsPage.getTitle()).toMatch(/Monthly Tasks/);
    });

    it('should load create MonthlyTask page', async () => {
        await monthlyTaskComponentsPage.clickOnCreateButton();
        monthlyTaskUpdatePage = new MonthlyTaskUpdatePage();
        expect(await monthlyTaskUpdatePage.getPageTitle()).toMatch(/Create or edit a Monthly Task/);
        await monthlyTaskUpdatePage.cancel();
    });

    it('should create and save MonthlyTasks', async () => {
        await monthlyTaskComponentsPage.clickOnCreateButton();
        await monthlyTaskUpdatePage.monthTypeSelectLastOption();
        await monthlyTaskUpdatePage.setTaskInput('task');
        expect(await monthlyTaskUpdatePage.getTaskInput()).toMatch('task');
        await monthlyTaskUpdatePage.setDescriptionInput('description');
        expect(await monthlyTaskUpdatePage.getDescriptionInput()).toMatch('description');
        const selectedCompleted = monthlyTaskUpdatePage.getCompletedInput();
        if (await selectedCompleted.isSelected()) {
            await monthlyTaskUpdatePage.getCompletedInput().click();
            expect(await monthlyTaskUpdatePage.getCompletedInput().isSelected()).toBeFalsy();
        } else {
            await monthlyTaskUpdatePage.getCompletedInput().click();
            expect(await monthlyTaskUpdatePage.getCompletedInput().isSelected()).toBeTruthy();
        }
        await monthlyTaskUpdatePage.userSelectLastOption();
        await monthlyTaskUpdatePage.save();
        expect(await monthlyTaskUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
