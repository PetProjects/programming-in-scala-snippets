import org.scalatest.FunSuite

/**
  * Created by Igor_Dobrovolskiy on 05.07.2017.
  */
class RationalTest extends FunSuite {

  test("testMax") {
    val a = new Rational(1, 3)
    val b = new Rational(2, 5)

    assert((a max b) === b)
  }

  test("test2") {
    val a = new Rational(1, 3)
    val b = new Rational(2, 5)

    val expected = new Rational(2, 15)

    assert((a * b) === expected)
  }

}
