import mt.um.edu.{SingletonSolver, Sudoku}

object Main {
  def main(args: Array[String]): Unit = {

    val example1 = new Sudoku(List(
      List(0, 0, 5, 0, 0, 6, 3, 0, 0),
      List(0, 0, 0, 0, 0, 0, 4, 0, 0),
      List(9, 8, 0, 7, 4, 0, 0, 0, 5),
      List(1, 0, 0, 0, 7, 0, 9, 0, 0),
      List(0, 0, 9, 5, 0, 1, 6, 0, 0),
      List(0, 0, 8, 0, 2, 0, 0, 0, 7),
      List(6, 0, 0, 0, 1, 8, 0, 9, 3),
      List(0, 0, 1, 0, 0, 0, 0, 0, 0),
      List(0, 0, 4, 2, 0, 0, 5, 0, 0)
    ))
    val example2 = new Sudoku(List(
      List(0, 4, 0, 7, 0, 2, 8, 0, 0),
      List(0, 0, 0, 0, 0, 0, 2, 0, 9),
      List(9, 2, 8, 0, 4, 0, 0, 0, 0),
      List(0, 0, 3, 8, 0, 7, 0, 1, 0),
      List(0, 0, 0, 0, 2, 0, 0, 0, 0),
      List(0, 7, 0, 9, 0, 1, 4, 0, 0),
      List(0, 0, 0, 0, 5, 0, 1, 9, 3),
      List(5, 0, 6, 0, 0, 0, 0, 0, 0),
      List(0, 0, 9, 1, 0, 4, 0, 8, 5)
    ))

    println("-----------------------------------")
    println("ROWS TEST")
    println("-----------------------------------")
    println("Row 0:    " + example1.row(0))
    println("Row 1:    " + example1.row(1))
    println("Row 2:    " + example1.row(2))
    println("-----------------------------------")
    println("COLUMNS TEST")
    println("-----------------------------------")
    println("Column 0: " + example1.column(0))
    println("Column 1: " + example1.column(1))
    println("Column 2: " + example1.column(2))
    println("-----------------------------------")
    println("BLOCKS TEST")
    println("-----------------------------------")
    println("Block 0:  " + example1.block(0))
    println("Block 1:  " + example1.block(1))
    println("Block 2:  " + example1.block(2))
    println("-----------------------------------Puzzle 1")
    println(example1)
    println("-----------------------------------Solution for Puzzle 1")
    println(new SingletonSolver().solve(example1))
    println("-----------------------------------Puzzle 2")
    println(example2)
    println("-----------------------------------Solution for Puzzle 2")
    println(new SingletonSolver().solve(example2))
    println("-----------------------------------")
  }
}
