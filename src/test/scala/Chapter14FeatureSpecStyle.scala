import org.scalatest.{FeatureSpec, GivenWhenThen}

/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */
class Chapter14FeatureSpecStyle extends FeatureSpec with GivenWhenThen {
  feature("TV power button"){
    scenario("User presses power button when TV is off"){
      Given("a TV ser that is switched off")
      When("the power button is pressed")
      Then("the TV should switch on")

      pending
    }
  }
}

object Chapter14FeatureSpecStyle extends App {
  println((new Chapter14FeatureSpecStyle).execute())
}