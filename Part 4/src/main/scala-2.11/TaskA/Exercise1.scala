package TaskA

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object Exercise1 {

  val separator1 = "   \t,\t"  // used in output
  val separator2 = "   \t|\t Average: "  // used in output

  object ActorA {
    case object Start
    case object NextF
    case object NextG
    case object SendValue
  }

  object ActorB {
    case class NextValue(x: Long)
  }

  class ActorA(processB: ActorRef) extends Actor with ActorLogging {

    import TaskA.Exercise1.ActorA._

    def generator(x: Long): Receive = {
      case NextF => {
        // Applies function 'f' to the value
        if (x == 0) context.become(generator(1000))
        else context.become(generator(x * 2))
        self ! SendValue
        self ! NextG
      }
      case NextG => {
        // Applies function 'g' to the value
        context.become(generator(x / 3))
        self ! SendValue
        self ! NextF
      }
      case SendValue => processB ! ActorB.NextValue(x)
      case _ => log.error("Invalid message at Actor A generator.")
    }

    override def receive: Receive = {
      case Start => {
        context.become(generator(0))
        self ! SendValue // send first value (zero)
        self ! NextF // first function is F
      }
      case _ => log.error("Invalid message at Actor A receive")
    }
  }

  class ActorB extends Actor with ActorLogging {

    import ActorB._

    def average(values: List[Long]): Long = {
      assert(values.size == 3)
      (values(0) + values(1) + values(2)) / values.size
    }

    def valueHandler(values: List[Long]): Receive = {
      case NextValue(x: Long) => {
        if (values.size == 2) {
          log.info(values(0) + separator1 + values(1) + separator1 + x + separator2 + average(x :: values))
          context.become(valueHandler(Nil)) // reset list
        }
        else if (values.size < 2) context.become(valueHandler(values ++ List(x))) // add new value
        else log.error("Invalid list size at Actor B valueHandler.")
      }
      case _ => log.error("Invalid message at Actor B value handler.")
    }

    override def receive: Receive = valueHandler(Nil)
  }

}

object Main {

  def main(args: Array[String]): Unit = {

    import Exercise1.{ActorA, ActorB}

    val system = ActorSystem("Exercise1")

    val actorB: ActorRef = system.actorOf(Props[ActorB], "ActorB")
    val actorA: ActorRef = system.actorOf(Props(new ActorA(actorB)), "ActorA")
    actorA ! ActorA.Start
  }
}
