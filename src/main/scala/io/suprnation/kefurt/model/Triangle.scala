package io.suprnation.kefurt.model

sealed trait Triangle {
  val value: Int
  val minimalPath: List[Int]
}

case class Node(left: Triangle, right: Triangle, value: Int, minimalPath: List[Int]) extends Triangle
case class End(value: Int) extends Triangle {
  override val minimalPath: List[Int] = List(value)
}
