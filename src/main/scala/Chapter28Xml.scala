/**
  * Created by Igor_Dobrovolskiy on 17.07.2017.
  */

import scala.xml.XML

abstract class CCTherm {
  val description: String
  val yearMade: Int
  val dateObtained: String
  val bookPrice: Int // in US cents
  val purchasePrice: Int // in US cents
  val condition: Int // 1 to 10

  override def toString = description

  def toXML =
    <cctherm condition={condition.toString}>
      <description>{description}</description>
      <yearMade>{yearMade}</yearMade>
      <dateObtained>{dateObtained}</dateObtained>
      <bookPrice>{bookPrice}</bookPrice>
      <purchasePrice>{purchasePrice}</purchasePrice>
      <comment>{{just to show escaping braces}}</comment>
    </cctherm>

  override def equals(obj: scala.Any): Boolean =
    obj match {
      case other: CCTherm => other.description == description &&
        other.yearMade == yearMade &&
        other.dateObtained == dateObtained &&
        other.bookPrice == bookPrice &&
        other.purchasePrice == purchasePrice &&
        other.condition == condition
      case _ => false
    }
}

object CCTherm {
  def apply(node: scala.xml.Node): CCTherm =
    new CCTherm {
      val description = (node \ "description").text
      val yearMade = (node \ "yearMade").text.toInt
      val dateObtained = (node \ "dateObtained").text
      val bookPrice = (node \ "bookPrice").text.toInt
      val purchasePrice = (node \ "purchasePrice").text.toInt
      val condition = (node \ "@condition").text.toInt
    }
}

object Chapter28Xml extends App {
  val therm = new CCTherm {
    val description = "hot dog #5"
    val yearMade = 1952
    val dateObtained = "March 14, 2006"
    val bookPrice = 2199
    val purchasePrice = 500
    val condition = 9
  }

  val xml = therm.toXML
  println(xml)

  val therm2 = CCTherm(xml)
  println(therm2, therm == therm2)

  val e =
    <a>
      <b>
        <a>1</a>
      </b>
      <c>
        <d par="7">3</d>
      </c>
      <a>4</a>
    </a>

  println(e \ "a")
  println("-" * 20)
  println((e \\ "a").mkString("\n"))
  println("-" * 20)
  println(e \ "@par")
  println("-" * 20)
  println(e \\ "@par")
  println("-" * 20)
  println(e \\ "d" \ "@par")

  XML.save("therm1.xml", xml, enc="utf-8", xmlDecl=true)
  val xml2 = XML.loadFile("therm1.xml")
  val therm3 = CCTherm(xml2)
  println(therm3, therm == therm3)

  val catalog =
    <catalog>
      <cctherm>
        <description>hot dog #5</description>
        <yearMade>1952</yearMade>
        <dateObtained>March 14, 2006</dateObtained>
        <bookPrice>2199</bookPrice>
        <purchasePrice>500</purchasePrice>
        <condition>9</condition>
      </cctherm>
      <cctherm>
        <description>Sprite Boy</description>
        <yearMade>1964</yearMade>
        <dateObtained>April 28, 2003</dateObtained>
        <bookPrice>1695</bookPrice>
        <purchasePrice>595</purchasePrice>
        <condition>5</condition>
      </cctherm>
    </catalog>

  println("-" * 20)

  catalog match {
    case <catalog>{therms @ _*}</catalog> =>
      for (therm <- therms)
        println("processing: " + (therm \ "description").text)
  }

  println("-" * 20)

  catalog match {
    case <catalog>{therms @ _*}</catalog> =>
      for (therm @ <cctherm>{_*}</cctherm> <- therms)
        println("processing: " + (therm \ "description").text)
  }
}
