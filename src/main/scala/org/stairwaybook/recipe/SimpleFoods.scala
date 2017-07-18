package org.stairwaybook.recipe

/**
  * Created by Igor_Dobrovolskiy on 18.07.2017.
  */

trait SimpleFoods {

  object Pear extends Food("Pear")

  def allFoods = List(Apple, Pear)

  def allCategories = Nil
}
