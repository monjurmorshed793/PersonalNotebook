import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { YearlyTaskComponentsPage, YearlyTaskUpdatePage } from './yearly-task.page-object';

describe('YearlyTask e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let yearlyTaskUpdatePage: YearlyTaskUpdatePage;
    let yearlyTaskComponentsPage: YearlyTaskComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load YearlyTasks', async () => {
        await navBarPage.goToEntity('yearly-task');
        yearlyTaskComponentsPage = new YearlyTaskComponentsPage();
        expect(await yearlyTaskComponentsPage.getTitle()).toMatch(/Yearly Tasks/);
    });

    it('should load create YearlyTask page', async () => {
        await yearlyTaskComponentsPage.clickOnCreateButton();
        yearlyTaskUpdatePage = new YearlyTaskUpdatePage();
        expect(await yearlyTaskUpdatePage.getPageTitle()).toMatch(/Create or edit a Yearly Task/);
        await yearlyTaskUpdatePage.cancel();
    });

    it('should create and save YearlyTasks', async () => {
        await yearlyTaskComponentsPage.clickOnCreateButton();
        await yearlyTaskUpdatePage.setYearInput('5');
        expect(await yearlyTaskUpdatePage.getYearInput()).toMatch('5');
        await yearlyTaskUpdatePage.setTaskInput('task');
        expect(await yearlyTaskUpdatePage.getTaskInput()).toMatch('task');
        await yearlyTaskUpdatePage.setDescriptionInput('description');
        expect(await yearlyTaskUpdatePage.getDescriptionInput()).toMatch('description');
        const selectedCompleted = yearlyTaskUpdatePage.getCompletedInput();
        if (await selectedCompleted.isSelected()) {
            await yearlyTaskUpdatePage.getCompletedInput().click();
            expect(await yearlyTaskUpdatePage.getCompletedInput().isSelected()).toBeFalsy();
        } else {
            await yearlyTaskUpdatePage.getCompletedInput().click();
            expect(await yearlyTaskUpdatePage.getCompletedInput().isSelected()).toBeTruthy();
        }
        await yearlyTaskUpdatePage.userSelectLastOption();
        await yearlyTaskUpdatePage.save();
        expect(await yearlyTaskUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
