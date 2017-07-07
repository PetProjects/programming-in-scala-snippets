import org.scalatest.{DiagrammedAssertions, FunSuite}
import Element._

/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */
class Chapter14UnitTestStyle extends FunSuite {
  test("Element's width is correct after construction") {
    val expectedWidth = 10
    val e = elem(' ', width = expectedWidth, 1)

    assertResult(expectedWidth) {
      e.width
    }
  }

  test("Element with illegal width fails on construction") {
    assertThrows[IllegalArgumentException] {
      elem(' ', -2, 1)
    }
  }

  test("Check how intersept works") {
    val caught = intercept[ArithmeticException] {
      1 / 0
    }

    assert(caught.getMessage == "/ by zero")
  }
}


object Chapter14UnitTestStyle extends App {
  import org.scalatest.Assertions._

//  assert(1===3)
//  assert(List(1, 2, 3).contains(4))
}