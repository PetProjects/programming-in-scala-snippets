import java.io.File
import scala.util.control.Breaks._

/**
  * Created by Igor_Dobrovolskiy on 05.07.2017.
  */

object Chapter07BuiltinControlStructures extends App {
  val files = (new File("./src/main/scala/")).listFiles

  for (file <- files)
    println(file)

  println

  for (file <- files
       if file.isFile
       if file.getName.endsWith(".scala")
  ) println(file)

  println

  def fileLines(file: java.io.File): List[String] =
    scala.io.Source.fromFile(file).getLines().toList

  for (file <- files
       if file.isFile
       if file.getName.endsWith(".scala");
       line <- fileLines(file)
       if line.trim.matches(".*gcd.*")
  ) println(s"$file: ${line.trim}")

  println

  for {file <- files
       if file.isFile
       if file.getName.endsWith(".scala");
       line <- fileLines(file)
       if line.trim.matches(".*for.*")
  } println(s"$file: ${line.trim}")

  println

  for {file <- files
       if file.isFile
       if file.getName.endsWith(".scala");
       line <- fileLines(file)
       trimmedLine = line.trim
       if trimmedLine.matches(".*java\\..*")
  } println(s"$file: $trimmedLine")

  println

  val l = List(1, 2, 3, 4)

  breakable {
    var i = 0
    while (true) {
      if (l(i) < 3) println(i)
      else break
      i += 1
    }
  }
}
