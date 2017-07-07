import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers, MustMatchers}
import Element._

/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */
class Chapter14BDDStyle extends FlatSpec with Matchers {
  "A UniformElement" should "have a width equal to the passed value" in {
    val expectedWidth = 2

    val ele = elem('x', expectedWidth, 3)
    ele.width should be(expectedWidth)
  }

  it should "have a height equal to the passed value" in {
    val expectedHeight = 3

    val ele = elem('x', 2, expectedHeight)
    ele.height should be(expectedHeight)
  }

  it should "throw an IAE exception in case of negative width" in {
    an[IllegalArgumentException] should be thrownBy {
      elem(' ', -3, 1)
      //elem(' ', 1, 1)
    }
  }
}

class Chapter14BDDStyle2 extends FlatSpec with MustMatchers with BeforeAndAfter {
  var e: Element = null

  before {
    e = elem(' ', 2, 5)
  }

  "Two the same UniformElement-s" must "have old width when applied above" in {
    assert((e above e).width === e.width)
  }

  it must "have double height when applied above" in {
    assert((e above e).height === e.height * 2)
  }

  it must "have old height when applied beside" in {
    (e beside e).height must be(e.height)
  }

  it must "have double width when applied beside" in {
    (e beside e).width must be(e.width * 2)
  }

  "Check must contain key" must "work" in {
    Map('a' -> 1, 'b' -> 2, 'c' -> 4) must contain key 'c'
  }

  "One more test" should "be written ... but not yet" in (pending)
}

object Chapter14BDDStyle extends App {
  println((new Chapter14BDDStyle).execute())
  println((new Chapter14BDDStyle2).execute())
}