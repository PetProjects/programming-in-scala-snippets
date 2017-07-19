import org.scalatest.WordSpec
import org.scalatest.prop.PropertyChecks
import org.scalatest.MustMatchers._
import Element._
import org.scalacheck.Gen

/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */
class Chapter14ScalaCheckStyle extends WordSpec with PropertyChecks {
  val genAppropriate: Gen[Int] = Gen.choose(0, Int.MaxValue >> 4) //limit to have no out of memory

  "elem result" must {
    "have passed width" in {
      forAll(genAppropriate) {
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