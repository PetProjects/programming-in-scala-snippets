import org.scalatest.WordSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.MustMatchers._
import Element._

/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */
class Chapter14ScalaCheckStyle extends WordSpec with PropertyChecks {
  "elem result" must {
    "have passed width" in {
      forAll{
        (w: Int) => whenever(w > 0) {
          elem(' ', w, 3).width must equal (w)
        }
      }
    }
  }
}


object Chapter14ScalaCheckStyle extends App {
  println((new Chapter14ScalaCheckStyle).execute())
}