package TaskB.Tests

import TaskB.Exercise2._
import akka.actor.{ActorSystem, Props}

object Demux2 {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Exercise2")

    val a = system.actorOf(Props(new Wire(false)), "A")
    val c = system.actorOf(Props(new Wire(false)), "C")
    val out = List(
      system.actorOf(Props(new Wire(false)), "out1"),
      system.actorOf(Props(new Wire(false)), "out0")
    )

    val demux2 = system.actorOf(Props(new Demux2(a, c, out(0), out(1))), "demux2")

    val a_probe = system.actorOf(Props(new Probe("a_probe", a)), "a_probe")
    val c_probe = system.actorOf(Props(new Probe("c_probe", c)), "c_probe")
    val out_probe = List(
      system.actorOf(Props(new Probe("out_probe1", out(0))), "out_probe1"), // for out1
      system.actorOf(Props(new Probe("out_probe0", out(1))), "out_probe0")  // for out0
    )

    def test(testDetails: String, boolA: Boolean, boolC: Boolean) = {
      println("\n" + testDetails)
      println("Setting input... (intermediary results)")
      a ! boolA
      Thread.sleep(1000)
      println("Setting control... (final results)")
      c ! boolC
      Thread.sleep(3000) // enough time for test to end
    }

    Thread.sleep(2000) // enough time for actors to be created
    test("Test 1 (false, false)", false, false)
    test("Test 2 (false, true)",  false, true)
    test("Test 3 (true, false)",  true,  false)
    test("Test 4 (true, true)",   true,  true)

    system.terminate()
  }
}
