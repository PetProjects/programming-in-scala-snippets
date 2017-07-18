package org.stairwaybook.recipe

/**
  * Created by Igor_Dobrovolskiy on 18.07.2017.
  */

object SimpleBrowser extends Browser {
//  val database = SimpleDatabase
  val database: SimpleDatabase.type = SimpleDatabase
}