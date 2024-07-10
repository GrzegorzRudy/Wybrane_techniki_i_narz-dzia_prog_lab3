package com.company.enroller.e2e.meetingsManagement;

import com.company.enroller.e2e.BaseTests;
import com.company.enroller.e2e.Const;
import com.company.enroller.e2e.authentication.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

public class MeetingsTests extends BaseTests {

    WebDriver driver;
    MeetingsPage page;
    LoginPage loginPage;

    @BeforeEach
    void setup() {
        this.dbInit();
        this.driver = WebDriverManager.chromedriver().create();
        this.page = new MeetingsPage(driver);
        this.loginPage = new LoginPage(driver);
        this.page.get(Const.HOME_PAGE);
    }


    @Test
    @DisplayName("[SPOTKANIA.1] The meeting should be added to your meeting list. It should contain a title and description.")
    void addNewMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_III_TITLE, Const.MEETING_DESC);
        // Asserts
        assertThat(this.page.getMeetingByTitle(Const.MEETING_III_TITLE)).isNotNull();

        // TODO: Dodaj sprawdzenie czy poprawnie został dodany opis.
        assertThat(this.page.getMeetingDescriptionByTitle(Const.MEETING_III_TITLE)).isEqualTo(Const.MEETING_DESC);

        this.page.sleep(1);
        // TODO: Dodaj sprawdzenie czy zgadza się aktualna liczba spotkań.
        try {
            Thread.sleep(5000); // Zatrzymaj wykonanie na 5 sekund (5000 milisekund)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       assertThat(this.page.getMeetingNumber()).isEqualTo(Const.ACTUAL_MEETING_NUMBER + 1);
    }

    // @Test
    // TODO: Sprawdź czy użytkownik może dodać spotkanie bez nazwy. Załóż że nie ma takiej możliwości a warunkiem
    //  jest nieaktywny przycisk "Dodaj".
    @Test
    @DisplayName("Spotkania bez nazwy")
    void addMeetingWithoutTitle() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.MeetingName("", Const.MEETING_DESC); // brak nazwy
        assertThat(this.page.AddButtonBoolean()).isFalse();
    }

    // @Test
    // TODO: Sprawdź czy użytkownik może poprawnie zapisać się do spotkania.
    @Test
    @DisplayName("Poprawność zapisu na spotkanie")
    void signUpForMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.userSignUp(Const.MEETING_I_TITLE);
        assertThat(this.page.isUserAMemberOfMeeting(Const.USER_I_NAME, Const.MEETING_I_TITLE)).isTrue();
    }



    // @Test
    // TODO: Sprawdź czy użytkownik może usunąć puste spotkanie.
    @Test
    @DisplayName("Kasowanie spotkania.")
    void deleteEmptyMeeting() {
        this.loginPage.loginAs(Const.USER_I_NAME);
        this.page.addNewMeeting(Const.MEETING_TO_DELETE, Const.MEETING_DESC);
        assertThat(this.page.getMeetingByTitle(Const.MEETING_TO_DELETE)).isNotNull();
        this.page.deleteMeeting(Const.MEETING_TO_DELETE);
        assertThat(this.page.getMeetingByTitle(Const.MEETING_TO_DELETE)).isNull();
    }

    @AfterEach
    void exit() {
        this.page.quit();
        this.removeAllMeeting();
    }

}
