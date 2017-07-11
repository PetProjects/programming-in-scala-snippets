import org.stairwaybook.simulation.CircuitSimulation

/**
  * Created by Igor_Dobrovolskiy on 11.07.2017.
  */

class Time {
  private[this] var h = 12
  private[this] var m = 0

  def hour: Int = h

  def hour_=(x: Int) = {
    require(0 <= x && x < 24)
    h = x
  }

  def minute = m

  def minute_=(x: Int) = {
    require(0 <= x && x < 60)
    m = x
  }

  override def toString: String = f"$h%02d:$m%02d"
}

object Chapter18MutableObjects extends App {
  val t = new Time
  t.minute = 30
  println(t)

  var i = 1

  val a, b, c = {i += 1; i}
  println(a, b, c)


  object MySimulation extends CircuitSimulation {
    def InverterDelay: Int = 1

    def AndGateDelay: Int = 3

    def OrGateDelay: Int = 5
  }

  import MySimulation._
  val input1, input2, sum, carry = new Wire
  probe("sum", sum)
  probe("carry", carry)
  halfAdder(input1, input2, sum, carry)

  input1 setSignal true
  run()

  input2 setSignal true
  run()
}
