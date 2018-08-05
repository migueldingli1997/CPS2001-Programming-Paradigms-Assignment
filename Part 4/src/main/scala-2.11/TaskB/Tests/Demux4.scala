package TaskB.Tests

import TaskB.Exercise2._
import akka.actor.{ActorSystem, Props}

object Demux4 {

  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Exercise2")

    val a = system.actorOf(Props(new Wire(false)), "a")
    val c = List(
      system.actorOf(Props(new Wire(false)), "s1"),
      system.actorOf(Props(new Wire(false)), "s0")
    )
    val out = List(
      system.actorOf(Props(new Wire(false)), "out3"),
      system.actorOf(Props(new Wire(false)), "out2"),
      system.actorOf(Props(new Wire(false)), "out1"),
      system.actorOf(Props(new Wire(false)), "out0")
    )

    val demux = system.actorOf(Props(new Demux(a, c, out)), "demux")

    val a_probe = system.actorOf(Props(new Probe("a_probe", a)), "a_probe")
    val c_probe = List(
      system.actorOf(Props(new Probe("c_probe1", c(0))), "c_probe1"), // for c1
      system.actorOf(Props(new Probe("c_probe0", c(1))), "c_probe0")  // for c0
    )
    val out_probe = List(
      system.actorOf(Props(new Probe("out_probe3", out(0))), "out_probe3"), // for out3
      system.actorOf(Props(new Probe("out_probe2", out(1))), "out_probe2"), // for out2
      system.actorOf(Props(new Probe("out_probe1", out(2))), "out_probe1"), // for out1
      system.actorOf(Props(new Probe("out_probe0", out(3))), "out_probe0")  // for out0
    )

    def test(testDetails: String, boolA: Boolean, boolC1: Boolean, boolC0: Boolean) = {
      println("\n" + testDetails)
      println("Setting input... (intermediary results)")
      a ! boolA
      Thread.sleep(1000)
      println("Setting control1... (intermediary results)")
      c(0) ! boolC1
      Thread.sleep(1000)
      println("Setting control0... (final results)")
      c(1) ! boolC0
      Thread.sleep(3000) // enough time for test to end
    }

    Thread.sleep(2000) // enough time for actors to be created
    test("Test 1 (false, false, false)", false, false, false)
    test("Test 2 (false, false, true)",  false, false, true)
    test("Test 3 (false, true, false)",  false, true,  false)
    test("Test 4 (false, true, true)",   false, true,  true)
    test("Test 5 (true, false, false)",  true,  false, false)
    test("Test 6 (true, false, true)",   true,  false, true)
    test("Test 7 (true, true, false)",   true,  true,  false)
    test("Test 8 (true, true, true)",    true,  true,  true)

    system.terminate()
  }
}
