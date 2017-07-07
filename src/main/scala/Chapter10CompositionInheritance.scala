import Element.elem

/**
  * Created by Igor_Dobrovolskiy on 06.07.2017.
  */
abstract class Element {
  def contents: Array[String] //abstract

  def height: Int = contents.length

  def width: Int = if (contents.length == 0) 0 else contents(0).length

  def above(that: Element): Element = {
    val this1 = this widen that.width
    val that1 = that widen width
    elem(this1.contents ++ that1.contents)
  }

  /** first impelementation */
  //  def beside(that: Element): Element = {
  //    require(this.contents.length == that.contents.length)
  //
  //    val contents = new Array[String](this.contents.length)
  //    for (i <- 0 until this.contents.length)
  //      contents(i) = this.contents(i) + that.contents(i)
  //
  //    new ArrayElement(contents)
  //  }

  /** Better implementation */
  def beside(that: Element): Element = {
    val this1 = this heighten that.height
    val that1 = that heighten this.height
    elem(
      for ((line1, line2) <- this1.contents zip that1.contents)
        yield line1 + line2
    )
  }

  def widen(w: Int): Element =
    if (w <= width) this
    else {
      val left = elem(' ', (w - width) / 2, height)
      val right = elem(' ', w - width - left.width, height)
      left beside this beside right
    }

  def heighten(h: Int): Element =
    if (h <= height) this
    else {
      val top = elem(' ', width, (h - height) / 2)
      val bot = elem(' ', width, h - height - top.height)
      top above this above bot
    }

  override def toString = contents mkString "\n"
}

object Element {

  /** Longer version of ArrayElement */
  //class ArrayElement(conts: Array[String]) extends Element{
  //  val contents: Array[String] = conts
  //}

  private class ArrayElement(val contents: Array[String]) extends Element

  /** First implementation of LineElement */
  //class LineElement(s: String) extends ArrayElement(Array(s)) {
  //  override def height: Int = 1
  //
  //  override def width: Int = s.length
  //}

  private class LineElement(s: String) extends Element {
    override def height: Int = 1

    override def width: Int = s.length

    val contents: Array[String] = Array(s)
  }

  private class UniformElement(ch: Char,
                               override val width: Int,
                               override val height: Int
                              ) extends Element {
    require(width >= 0)
    require(height >= 0)

    private val line = ch.toString * width

    def contents: Array[String] = Array.fill(height)(line)
  }

  def elem(contents: Array[String]): Element = new ArrayElement(contents)

  def elem(line: String): Element = new LineElement(line)

  def elem(c: Char, width: Int, height: Int): Element = new UniformElement(c, width = width, height = height)

}

object Spiral {
  val space = elem(" ")
  val corner = elem("+")

  def spiral(nEdges: Int, direction: Int): Element = {
    if (nEdges == 1) corner
    else {
      val sp = spiral(nEdges - 1, (direction + 3) % 4)

      def verticalBar = elem('|', 1, sp.height)

      def horizontalBar = elem('-', sp.width, 1)

      if (direction == 0)
        (corner beside horizontalBar) above (sp beside space)
      else if (direction == 1)
        (sp above space) beside (corner above verticalBar)
      else if (direction == 2)
        (space beside sp) above (horizontalBar beside corner)
      else
        (verticalBar above corner) beside (space above sp)
    }
  }
}

object Chapter10CompositionInheritance extends App {
  println(Spiral.spiral(6, 0))
  println
  println(Spiral.spiral(11, 0))
  println
  println(Spiral.spiral(17, 0))
  println
  println(Spiral.spiral(33, 0))
}
