/**
  * Created by Igor_Dobrovolskiy on 04.07.2017.
  */

import scala.collection.mutable

class ChecksumAccumulator {
  private var sum = 0

  def add(b: Byte): Unit = { sum += b }

  def checksum(): Int = ~(sum & 0xFF) + 1
}

object Chapter04ClassesObjects {
  def main(args: Array[String]): Unit = {
    val acc = new ChecksumAccumulator()
    acc.add(2)
    acc.add(-128)
    println(acc.checksum)

    println(1
      + 2
      + 3)

    Console println
      1
    +2

    val i: Unit => Int = _ => { 3; 5 }
    val i2: Unit => Int = _ => 3; 5

    println(i(), i2())

    //    val i3 = () => {3;5}
    //    val i4: () => 3;5

    val a = mutable.WeakHashMap(1 -> "A")
    println(a)
  }
}

object TraitBasedMain extends App {
  println("Hello, trait! )")
  println(args.mkString(", "))
}