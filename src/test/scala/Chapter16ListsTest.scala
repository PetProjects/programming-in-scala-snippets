import org.scalatest.FunSuite

/**
  * Created by Igor_Dobrovolskiy on 10.07.2017.
  */
class Chapter16ListsTest extends FunSuite {
  test("Test correspondence between 'splitAt' and 'drop' and 'take'"){
    val l = List(10, 3, 4, 7, 1)

    assert((l splitAt 2) === (l take 2, l drop 2))
  }

  test("Test zipWithIndex is equal to 'x.indices zip x'") {
    val abcde = "abcde".toList

    assert((abcde zip abcde.indices) === abcde.zipWithIndex)
  }

  test("Test zipWithIndex unzip is equal to '(x, x.indices)") {
    val abcde = "abcde".toList

    assert((abcde, abcde.indices) === abcde.zipWithIndex.unzip)
  }

  test("Test partition and filter interrelation"){

    val words = List("the", "quick", "brown", "fox")
    val p = (x: String) => x.length == 3

    assert((words partition p) === (words filter p, words filter (!p(_))))
  }

  test("Test span and takeWhile/dropWhile interrelation"){

    val words = List("the", "quick", "brown", "fox")
    val p = (x: String) => x.length == 3

    assert((words span p) === (words takeWhile p, words dropWhile p))
  }
}
