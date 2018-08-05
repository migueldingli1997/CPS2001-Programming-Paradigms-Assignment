package TaskB

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Exercise2 {

  val defaultState = false
  val unrecognizedWire_String = "{} received message from unrecognized wire."
  val invalidMessage_String = "{} received invalid message."

  case class AddComponent(wireName: String, actor: ActorRef)

  case class StateChange(wireName: String, state: Boolean)

  class Wire(var currentState: Boolean) extends Actor with ActorLogging {

    var associations = Map[ActorRef, String]()

    def receive: Actor.Receive = {
      case AddComponent(name: String, b: ActorRef) => {
        if (associations.contains(b)) log.error("{} is already subscribed to {}.", b.path.name, self.path.name)
        else associations += (b -> name)
      }
      case current: Boolean => {
        currentState = current
        associations.foreach(kv => kv._1 ! StateChange(kv._2, current))
      }
    }
  }

  class Probe(name: String, input0: ActorRef) extends Actor with ActorLogging {
    implicit val ec = context.dispatcher

    val inName: String = input0.path.name
    input0 ! AddComponent(inName, self)

    def receive = {
      case StateChange(wireName: String, current: Boolean) => wireName match {
        case `inName` => log.info("{} reads {}", name, current)
        case _ => log.error(unrecognizedWire_String, self.path.name)
      }
      case _ => log.error(invalidMessage_String, self.path.name)
    }
  }

  class Inverter(input0: ActorRef, output0: ActorRef) extends Actor with ActorLogging with SimConfig {
    implicit val ec = context.dispatcher

    val inName: String = "InvIn"  // name for input
    input0 ! AddComponent(inName, self)

    def receive = {
      case StateChange(wireName: String, newState: Boolean) => wireName match {
        case `inName` => context.system.scheduler.scheduleOnce(inverterDelay, output0, !newState)
        case _ => log.error(unrecognizedWire_String, self.path.name)
      }
      case _ => log.error(invalidMessage_String, self.path.name)
    }
  }

  class And(input0: ActorRef, input1: ActorRef, output0: ActorRef) extends Actor with ActorLogging with SimConfig {
    implicit val ec = context.dispatcher

    val in0Name: String = "AndIn0"  // name for 1st input
    val in1Name: String = "AndIn1"  // name for 2nd input
    input0 ! AddComponent(in0Name, self)
    input1 ! AddComponent(in1Name, self)

    def state(in0: Boolean, in1: Boolean): Receive = {
      case StateChange(wireName: String, newState: Boolean) => wireName match {
        case `in0Name` => {
          // If change was to in0, then in1 remains the same
          context.become(state(newState, in1))
          context.system.scheduler.scheduleOnce(andDelay, output0, newState && in1)
        }
        case `in1Name` => {
          // If change was to in1, then in0 remains the same
          context.become(state(in0, newState))
          context.system.scheduler.scheduleOnce(andDelay, output0, in0 && newState)
        }
        case _ => log.error(unrecognizedWire_String, self.path.name)
      }
      case _ => log.error(invalidMessage_String, self.path.name)
    }

    def receive = state(defaultState, defaultState)
  }

  class Or(input0: ActorRef, input1: ActorRef, output0: ActorRef) extends Actor with ActorLogging with SimConfig {
    implicit val ec = context.dispatcher

    val in0Name: String = "OrIn0"  // name for 1st input
    val in1Name: String = "OrIn1"  // name for 2nd input
    input0 ! AddComponent(in0Name, self)
    input1 ! AddComponent(in1Name, self)

    def state(in0: Boolean, in1: Boolean): Receive = {
      case StateChange(wireName: String, newState: Boolean) => wireName match {
        case `in0Name` => {
          // If change was to in0, then in1 remains the same
          context.become(state(newState, in1))
          context.system.scheduler.scheduleOnce(orDelay, output0, newState || in1)
        }
        case `in1Name` => {
          // If change was to in1, then in0 remains the same
          context.become(state(in0, newState))
          context.system.scheduler.scheduleOnce(orDelay, output0, in0 || newState)
        }
        case _ => log.error(unrecognizedWire_String, self.path.name)
      }
      case _ => log.error(invalidMessage_String, self.path.name)
    }

    def receive = state(defaultState, defaultState)
  }

  class OrAlt(input0: ActorRef, input1: ActorRef, output0: ActorRef) extends Actor with ActorLogging {

    val not0_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val not1_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val and_Out: ActorRef = context.actorOf(Props(new Wire(false)))

    val not0: ActorRef = context.actorOf(Props(new Inverter(input0, not0_Out)))
    val not1: ActorRef = context.actorOf(Props(new Inverter(input1, not1_Out)))
    val and: ActorRef = context.actorOf(Props(new And(not0_Out, not1_Out, and_Out)))
    val notAnd: ActorRef = context.actorOf(Props(new Inverter(and_Out, output0)))

    def receive: Receive = Actor.emptyBehavior
  }

  class HalfAdder(input0: ActorRef, input1: ActorRef, outputSum: ActorRef,
                  outputCarry: ActorRef) extends Actor with ActorLogging {

    val not0_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val not1_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val and_0Not1_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val and_1Not0_Out: ActorRef = context.actorOf(Props(new Wire(false)))

    val not0: ActorRef = context.actorOf(Props(new Inverter(input0, not0_Out)))
    val not1: ActorRef = context.actorOf(Props(new Inverter(input1, not1_Out)))
    val and_0Not1: ActorRef = context.actorOf(Props(new And(input0, not1_Out, and_0Not1_Out)))
    val and_1Not0: ActorRef = context.actorOf(Props(new And(input1, not0_Out, and_1Not0_Out)))

    val sum: ActorRef = context.actorOf(Props(new Or(and_0Not1_Out, and_1Not0_Out, outputSum)))
    val carry: ActorRef = context.actorOf(Props(new And(input0, input1, outputCarry)))

    def receive: Actor.Receive = Actor.emptyBehavior
  }

  class FullAdder(input0: ActorRef, input1: ActorRef, inputCarry: ActorRef, outputSum: ActorRef,
                  outputCarry: ActorRef) extends Actor with ActorLogging {

    val carry1_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val carry2_Out: ActorRef = context.actorOf(Props(new Wire(false)))
    val sum1_Out: ActorRef = context.actorOf(Props(new Wire(false)))

    val halfAdder1: ActorRef = context.actorOf(Props(new HalfAdder(input1, inputCarry, sum1_Out, carry1_Out)))
    val halfAdder2: ActorRef = context.actorOf(Props(new HalfAdder(input0, sum1_Out, outputSum, carry2_Out)))
    val or: ActorRef = context.actorOf(Props(new Or(carry1_Out, carry2_Out, outputCarry)))

    def receive: Actor.Receive = Actor.emptyBehavior
  }

  class Demux(input: ActorRef, controls: List[ActorRef], outputs: List[ActorRef]) extends Actor with ActorLogging {

    (controls, outputs) match {
      case (ctrl :: Nil, out1 :: out0 :: Nil) => {
        val demux_2 = context.actorOf(Props(new Demux2(input, ctrl, out1, out0)))
      }
      case (ctrl :: ctrls, outs) => {
        val demux2_out1 = context.actorOf(Props(new Wire(false)))
        val demux2_out0 = context.actorOf(Props(new Wire(false)))
        val demux2 = context.actorOf(Props(new Demux2(input, ctrl, demux2_out1, demux2_out0)))

        val outCount = outs.size
        val demux1 = context.actorOf(Props(new Demux(demux2_out1, ctrls, outs.take(outCount / 2))))
        val demux0 = context.actorOf(Props(new Demux(demux2_out0, ctrls, outs.drop(outCount / 2))))
      }
      case _ => log.error("Demux error. Invalid number of controls or outputs.")
    }

    def receive: Actor.Receive = Actor.emptyBehavior
  }

  class Demux2(input: ActorRef, control: ActorRef, output1: ActorRef,
               output0: ActorRef) extends Actor with ActorLogging {

    val notCtrl_Out: ActorRef = context.actorOf(Props(new Wire(false)))

    val notCtrl: ActorRef = context.actorOf(Props(new Inverter(control, notCtrl_Out)))
    val and0: ActorRef = context.actorOf(Props(new And(input, notCtrl_Out, output0)))
    val and1: ActorRef = context.actorOf(Props(new And(input, control, output1)))

    def receive: Actor.Receive = Actor.emptyBehavior
  }
}

object Main {

  import akka.actor.ActorSystem
  import TaskB.Exercise2._

  // Run main methods from 'Tests' for a variety of test outputs
  def main(args: Array[String]): Unit = {}
}