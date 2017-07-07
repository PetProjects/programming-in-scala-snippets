package object bobsdelights{
  def showFruit(fruit: Fruit) = {
    import fruit._
    println(name + "s are " + color)
  }
}