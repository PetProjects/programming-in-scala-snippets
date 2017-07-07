/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */

class Anchor(val value: String) extends AnyVal
class Style(val value: String) extends AnyVal
class Text(val value: String) extends AnyVal
class Html(val value: String) extends AnyVal


object Chapter11ScalaHierarchy extends App {
  val a: AnyRef = List()

  println(a##)

  def title(text: Text, anchor: Anchor, style: Style) = ???
}
