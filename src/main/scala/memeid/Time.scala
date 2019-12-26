package memeid.time

import java.util.concurrent.atomic.AtomicReference

import scala.annotation.tailrec

import cats.effect._

trait Time[F[_]] {
  /* A gregorian time monotonic timestamp. */
  def monotonic: F[Long]
}

object Time {

  def tick: Long =
    System.currentTimeMillis

  /* Class to ensure unique gregorian timestamps for a given `resolution`. It works by keeping a sequence of
   * generated IDs and shifting the `ts` value adding the `seq`. Users of this class must ensure to
   * stall timestamp generation if the seqids are all used for the given `resolution`, since `State#update`
   * will block until it finds a different timestamp to generate new monotonic values. */
  final case class State(seq: Short, ts: Long, resolution: Short) {
    /* Advance to next state, upping the sequence number. */
    def next: State = State((seq + 1).toShort, ts, resolution)

    /* Reset the state with a new tick. */
    def reset(newTs: Long): State =
      copy(seq = 0, ts = newTs)

    /* Update the state with a new tick. */
    @tailrec
    @SuppressWarnings(Array("scalafix:DisableSyntax.!="))
    def update(newTs: Long): State = {
      if (ts != newTs) {
        reset(newTs)
      } else {
        if (seq < resolution) {
          next
        } else {
          update(tick)
        }
      }
    }

    /* Compute the gregorian time monotonic timestamp for this state. */
    def timestamp: Long =
      seq + 100103040000000000L + (1000 * (ts + 2208988800000L))
  }

  object State {
    val empty: State = State(0, 0, 999)
  }

  /* We ensure atomic updates to the state with no locks using an atomic reference, which allows
   * us to stall timestamp generation if many concurrent timestamps are generated. */
  val state: AtomicReference[State] = new AtomicReference[State](State.empty)

  implicit def apply[F[_]: Sync]: Time[F] = new Time[F] {

    def monotonic: F[Long] = Sync[F].delay {
      state.updateAndGet(_.update(tick)).timestamp
    }
  }
}
