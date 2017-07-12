package org.stairwaybook.currencies

/**
  * Created by Igor_Dobrovolskiy on 12.07.2017.
  */
object Japan extends CurrencyZone {

  abstract class Yen extends AbstractCurrency {
    def designation = "JPY"
  }

  type Currency = Yen

  def make(yen: Long) = new Yen {
    val amount = yen
  }

  val Yen = make(1)
  val CurrencyUnit = Yen
}
