package io.suprnation.kefurt

import io.suprnation.kefurt.model.MinimalPath

object TriangleComputer {

  def computeMinimum(input: Triangle): Option[MinimalPath] = {
    if (input.isEmpty) {
      None
    } else {
      val bottom = input(input.length - 1).map(value => MinimalPath(value, List(value))) // last row (input.length - 1 == last index)
      val upperRows = input.take(input.length - 1) // input.length - 1 == ignore last

      upperRows
        .foldRight(bottom)((currentLine, previousLine) => {
          val selectedMinValues = computeMinOfTwoNeighboringValues(previousLine)
          selectedMinValues.zip(currentLine).map { case (minValue, currentValue) =>
            MinimalPath(minValue.sumValue + currentValue, minValue.path.prepended(currentValue))
          }
        })
        .headOption
    }
  }

  private def computeMinOfTwoNeighboringValues(line: Array[MinimalPath]): Array[MinimalPath] = {
    /*
    val line = [1, 2, 3, 4]
    line.zip(line.tail) === [(1,2),(2,3),(3,4)]
     */
    line.zip(line.tail).map(pair => if (pair._1.sumValue < pair._2.sumValue) pair._1 else pair._2)
  }

}
