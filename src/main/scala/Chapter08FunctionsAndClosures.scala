import java.time.{LocalDateTime, ZoneId}
import java.util.Calendar

/**
  * Created by Igor_Dobrovolskiy on 05.07.2017.
  */

object Chapter08FunctionsAndClosures extends App {
  val f = (_: Int) + (_: Int)
  println(f(1, 2))

  println

  val l = List(1, 2, 3, 4)
  l.foreach(println(_))

  println

  l.foreach(println _)

  println

  def sum(a: Int, b: Int, c: Int): Int = a + b + c

  val f2 = sum(1, _: Int, 3)

  println(f2(2))

  val dt = Calendar.getInstance.getTime

  def printTime(dt: java.util.Date = Calendar.getInstance.getTime, out: java.io.PrintStream = Console.out) = out.println(dt.toString)

  printTime()
  printTime(dt = java.util.Date.from(LocalDateTime.now().minusHours(1).atZone(ZoneId.systemDefault()).toInstant))
  printTime(out = Console.err)

  val jdt = new org.joda.time.LocalDateTime().minusHours(5)
  printTime(out = Console.err, dt = jdt.toDate)
}
