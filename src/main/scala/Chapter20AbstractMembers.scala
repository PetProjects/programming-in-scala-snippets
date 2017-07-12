import org.stairwaybook.currencies._

/**
  * Created by Igor_Dobrovolskiy on 12.07.2017.
  */

trait RationalTrait {
  val numerArg: Int
  val denomArg: Int
  require(denomArg != 0)
  private val g = gcd(numerArg, denomArg)
  val numer = numerArg / g
  val denom = denomArg / g

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  override def toString = s"$numer/$denom"
}

trait LazyRationalTrait {
  val numerArg: Int
  val denomArg: Int

  private lazy val g = {
    require(denomArg != 0)
    gcd(numerArg, denomArg)
  }
  lazy val numer = numerArg / g
  lazy val denom = denomArg / g

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  override def toString = s"$numer/$denom"
}

object Chapter20AbstractMembers extends App {
  /** Fails with exception because of initialization after checking require()-s */

  //  new RationalTrait {
  //    val denomArg: Int = 1
  //    val numerArg: Int = 2
  //  }

  /** Works fine */

  println(new {
    val denomArg: Int = 2
    val numerArg: Int = 1
  } with RationalTrait) //Pre-initialized fields in object definition

  println(new LazyRationalTrait {
    val denomArg: Int = 3
    val numerArg: Int = 2
  })

  /** pre-init fields */

  class RationalClass(n: Int, d: Int) extends {
    val numerArg: Int = n
    val denomArg: Int = d
  } //Pre-initialized fields in class definition
    with RationalTrait {
    def +(that: RationalClass) = new RationalClass(numer * that.numer + that.numer * denom, denom * that.denom)
  }

  object Demo {
    lazy val x: String = {
      println("initializing x");
      "demo"
    }
  }

  println("Init demo...")
  Demo
  println("Acess Demo.x ...")
  Demo.x


  class Food

  abstract class Animal {
    type SuitableFood <: Food

    def eat(food: SuitableFood)
  }

  class Grass extends Food

  class Cow extends Animal {
    type SuitableFood = Grass

    override def eat(food: Grass) = {}
  }

  class Fish extends Food

  //  new Cow eat (new Fish) //Error:(90, 16) type mismatch; found   : Chapter20AbstractMembers.Fish required: Chapter20AbstractMembers.Grass new Cow eat (new Fish)

  /** Path-dependent types */

  class DogFood extends Food

  class Dog extends Animal {
    type SuitableFood = DogFood

    override def eat(food: DogFood) = {}
  }

  val cow = new Cow
  val dog = new Dog
  println((new cow.SuitableFood).getClass == (new cow.SuitableFood).getClass)
  println((new cow.SuitableFood).getClass == (new dog.SuitableFood).getClass)

  /** Refinement types */
  class Pasture {
    val animals: List[Animal {type SuitableFood = Grass}] = Nil
  }

  object Direction extends Enumeration {
    val North, East, West, South = Value
  }

  for(d <- Direction.values) print(d + ", "); println()

  object Direction2 extends Enumeration {
    val North = Value("the North")
    val East = Value("the East")
    val West = Value("the West")
    val South = Value("the South")
  }

  for(d <- Direction2.values) print(d + ", "); println()

  val jpy = Japan.Yen from US.Dollar * 100
  val eur = Europe.Euro from jpy
  val usd = US.Dollar from eur

  println(jpy)
  println(eur)
  println(usd)

  //US.Dollar + Europe.Euro // not compile
  println(US.Dollar + US.Dollar from Europe.Euro)
}
