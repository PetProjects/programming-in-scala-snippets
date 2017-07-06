import java.io._

/**
  * Created by Igor_Dobrovolskiy on 06.07.2017.
  */
object Chapter09ControlAbstraction extends App {

  def sum(a: Int)(b: Int) = a + b

  def sum2(a: Int, b: Int) = a + b

  val fCurriedPartialApplied = sum(10) _
  val fUncurriedPartialApplied = sum2(10, _: Int)

  val fCurriedUncurried: Int => Int => Int = (a: Int) => sum2(a, _)
  val fUncurriedCurried: (Int, Int) => Int = fCurriedUncurried(_)(_)

  println(fCurriedPartialApplied, fUncurriedPartialApplied, fCurriedUncurried)

  println(fCurriedPartialApplied(1), fUncurriedPartialApplied(1), fCurriedUncurried(1))

  println(fCurriedUncurried(10)(1), fUncurriedCurried(10, 1))

  def withPrinterWriter(file: File)(op: PrintWriter => Unit) = {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    } finally {
      writer.close()
    }
  }

  withPrinterWriter(new File("./2.txt")) { w =>
    for (i <- 1 to 10) w.println(i)
  }

  println

  def do10Times(routine: () => Unit): Unit =
    for (i <- 1 to 10) routine()

  def do10Times_(routine: () => Unit): Unit = //BUG!
    for (i <- 1 to 10) routine

  def do10Times2(routine: => Unit): Unit =
    for (i <- 1 to 10) routine

  def do10Times3(routine: () => Unit): Unit =
    for (i <- 1 to 10) routine()

  def do10Times3_(routine: () => Unit): Unit = //BUG!
    for (i <- 1 to 10) routine

  do10Times { () =>
    print("+ ")
  }
  println

  do10Times_ { () =>
    print("+ ")
  }
  println

  do10Times2 {
    print("! ")
  }
  println

  do10Times3 { () =>
    print("% ")
  }
  println

  do10Times3_ { () =>
    print("% ")
  }
  println

}
