package io.suprnation.kefurt.model

sealed trait Triangle {
  val value: Int
}

case class Node(left: Triangle, right: Triangle, value: Int) extends Triangle
case class End(value: Int) extends Triangle
