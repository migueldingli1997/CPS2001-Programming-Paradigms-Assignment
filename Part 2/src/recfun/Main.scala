package recfun

object Main {

  /**
    * Task 1
    */
  def pascal(c: Int, r: Int): Int =
    if(c == r || c == 0) 1
    else pascal(c-1, r-1) + pascal(c, r-1)

  /**
    *  Task 2 - Part A
    */
  def fib(n: Long): Long =
    if(n <= 1) n
    else fib(n-1) + fib(n-2)

  /**
    *  Task 2 - Part B
    */
  def fastFib(x: Long ): Long = {

    def fibStep(y: (Long, Long)): (Long, Long) =
      (y._2, y._2 + y._1)

    def fibIter(c: Long, y: (Long, Long)): (Long, Long) =
      if (c == x) y
      else fibIter(c + 1, fibStep(y))

    if(x <= 1) x
    else fibIter(1, (0,1))._2
  }

  /**
    * Task 3
    */
  def balance(chars: List[Char]): Boolean = {
    def loop(chars: List[Char], count: Int): Int = {
      if(count >= 0 && chars.nonEmpty) {
        if(chars.head == '(') loop(chars.tail, count+1)       // Open paranthesis
        else if(chars.head == ')') loop(chars.tail, count-1)  // Close paranthesis
        else loop(chars.tail, count)                          // Not a parenthesis
      }
      else count // no more characters or negative count, so return '(' count
    }
    loop(chars, 0) == 0 // true if '(' count equal to ')' count
  }

  /**
    *  Task 4
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    if(money < 0 || coins.isEmpty) 0
    else if(money == 0) 1
    else countChange(money-coins.head, coins) + countChange(money, coins.tail)
  }
}