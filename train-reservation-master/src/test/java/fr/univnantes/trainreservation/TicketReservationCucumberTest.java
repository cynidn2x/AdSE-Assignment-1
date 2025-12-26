package fr.univnantes.trainreservation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/bdd.feature",
    glue = "fr.univnantes.trainreservation.stepdefinitions",
    plugin = {"pretty", "html:target/cucumber-reports.html"},
    tags = "@implementation"
)
public class TicketReservationCucumberTest {
}
