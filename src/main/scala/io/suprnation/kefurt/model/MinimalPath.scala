package io.suprnation.kefurt.model

import cats.Show

case class MinimalPath(sumValue: Int, path: List[Int])

object MinimalPath {
  implicit val minimalPathShow: Show[MinimalPath] = Show.show(minPath => s"${minPath.path.mkString(" + ")} = ${minPath.sumValue}")
}
