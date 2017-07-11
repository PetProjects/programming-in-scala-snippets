import scala.collection.immutable.{TreeMap, TreeSet}
import scala.collection.mutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer, Map}

/**
  * Created by Igor_Dobrovolskiy on 11.07.2017.
  */
object Chapter17OtherCollections extends App {
  val fiveDimArr = Array.tabulate[Int](2, 2, 2, 2, 2)(_ * 16 + _ * 8 + _ * 4 + _ * 2 + _)
  println(
    (
      for {
        fourDimArr <- fiveDimArr
        threeDimArr <- fourDimArr
        twoDimArr <- threeDimArr
        arr <- twoDimArr
      } yield arr.mkString("[", ",", "]")
      ).mkString("|")
  )

  val lb = ListBuffer(0, 1, 2, 3)
  lb += 4
  lb.insert(0, -1)
  -2 +=: lb
  println(lb)

  val ab = ArrayBuffer("aabcda".toCharArray: _*)
  ab -= 'a'
  ab -= 'b'
  ab += 'A'
  ab.remove(2)
  println(ab)

  println("abcde".forall(_ != 'f'))

  val s = "See Spot run. Run, Spot. Run!"
  val distWords = mutable.Set.empty[String]
  val distLcWords = mutable.Set.empty[String]
  for (word <- s.split("""[ ,.!]+""")) {
    distWords += word
    distLcWords += word.toLowerCase
  }
  println(distWords, distLcWords)
  println(distWords & distLcWords)
  distWords --= distLcWords
  println(distWords)

  val counts = Map.empty[String, Int]
  for (word <- s.split("[ ,.!]+")) {
    val lcWord = word.toLowerCase
    val currentCount = if (counts.contains(lcWord)) counts(lcWord) else 0
    counts += lcWord -> (currentCount + 1)
  }
  println(counts)
  println(counts ++ Seq("cheat word" -> 10, "ghost word" -> -1) - "run")
  for (k <- counts.keys) print(k + " ");
  println()
  println(counts.keySet)
  counts --= Seq("run", "see")
  counts += "RUNNNN!" -> 100500
  println(counts)

  println(Set().getClass)
  println(Set(1).getClass)
  println(Set(1, 2).getClass)
  println(Set(1, 2, 3, 4).getClass)
  println(Set(1, 2, 3, 4, 5).getClass)

  println(TreeSet(9, 3, 7, 1, 5, 8, -1))
  var tm = TreeMap(5 -> '5', 3 -> '3', 0 -> '0')
  tm += (2 -> '2')
  println(tm)

  /** Immutable -> Mutable -> Immutable */
  val chars: scala.collection.immutable.Set[Char] = "abcdefg".toSet
  val mutChars = mutable.Set.empty ++= chars
  val immutChars = Set.empty ++ mutChars

  /** Assigning Tuple2 to 2 vars vs. multiple assignment*/
  val (word, indx) = "word" -> 1 //actually pattern matching
  val word2, indx2 = "word2" -> 2
  println(s"($word, $indx) VS $word2, $indx2")
}
