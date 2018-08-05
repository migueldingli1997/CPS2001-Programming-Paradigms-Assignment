package mt.um.edu

class Sudoku(val grid: List[List[Int]]) {

  /*Assumptions:
    1) 0 <= row <= 8
    2) 0 <= col <= 8
    3) 0 <= block <= 8
    */

  def row(row: Int): Set[Int] =
    grid(row).toSet.diff(Set(0))

  def column(column: Int): Set[Int] =
    grid.map(_ (column)).toSet.diff(Set(0))

  def block(block: Int): Set[Int] = {
    val col1 = (block % 3) * 3 // starting column
    val row1 = (block / 3) * 3 // starting row

    // Get three columns from a particular row, starting from col1
    def getColumns(row: Int): List[Int] =
      List(grid(row)(col1), grid(row)(col1 + 1), grid(row)(col1 + 2))

    // Apply getColumns on three rows, starting from row1
    (getColumns(row1) ++ getColumns(row1 + 1) ++ getColumns(row1 + 2)).toSet.diff(Set(0))
  }

  override def toString() = {
    def rowOutput(row: List[Int]): String = {
      def valueOutput(value: Int): String = value match {
        case 0 => "_ "
        case n => n + " "
      }
      row.map(valueOutput).foldRight("\n")(_ + _)
    }
    grid.map(rowOutput).foldRight("")(_ + _)
  }
}