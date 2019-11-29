package memeid

import java.lang.Long
import java.util.{UUID => JUUID}

import cats.Show
import cats.instances.uuid._
import cats.kernel._
import cats.syntax.contravariant._

import memeid.JavaConverters._

sealed trait UUID {

  private[memeid] val juuid: JUUID

  @inline
  def msb: Long = juuid.getMostSignificantBits

  @inline
  def lsb: Long = juuid.getLeastSignificantBits

  @inline
  def variant: Int = juuid.variant

  @SuppressWarnings(Array("scalafix:Disable.equals", "scalafix:Disable.Any"))
  override def equals(obj: Any): Boolean = obj match {
    case x: UUID => Order[UUID].eqv(this, x)
    case _       => false
  }

  @SuppressWarnings(Array("scalafix:Disable.hashCode"))
  override def hashCode(): Int = Hash[UUID].hash(this)

  @SuppressWarnings(Array("scalafix:Disable.toString"))
  override def toString: String = Show[UUID].show(this)

}

object UUID {

  /**
   * The nil UUID is special form of UUID that is specified to have all 128 bits set to zero.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.7]]
   */
  case object Nil extends UUID { override val juuid: JUUID = new JUUID(0, 0) }

  /**
   * Version 1 UUIDs are those generated using a timestamp and the MAC address of the
   * computer on which it was generated.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V1 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * DCE Security version, with embedded POSIX UIDs.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V2 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Version 3 UUIDs are those generated by hashing a namespace identifier and name using
   * MD5 as the hashing algorithm.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V3 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Version 4 UUIDs are those generated using random numbers.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V4 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Version 5 UUIDs are those generated by hashing a namespace identifier and name using
   * SHA-1 as the hashing algorithm.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class V5 private[memeid] (override private[memeid] val juuid: JUUID) extends UUID

  /**
   * Not standard-version UUIDs. Includes the extracted version from the most significant bits.
   *
   * @see [[https://tools.ietf.org/html/rfc4122#section-4.1.3]]
   */
  final class UnknownVersion private[memeid] (
      val version: Int,
      override private[memeid] val juuid: JUUID
  ) extends UUID

  /* Constructors */

  def from(msb: Long, lsb: Long): UUID = new JUUID(msb, lsb).asScala

  /* Typeclass instances */

  implicit val UUIDHashOrderInstance: Order[UUID] with Hash[UUID] =
    new Order[UUID] with Hash[UUID] {

      override def hash(x: UUID): Int = Hash[JUUID].hash(x.juuid)

      override def compare(x: UUID, y: UUID): Int =
        Long.compareUnsigned(x.msb, y.msb) match {
          case 0 => Long.compareUnsigned(x.lsb, y.lsb)
          case x => x
        }

    }

  implicit val UUIDShowInstance: Show[UUID] = Show[JUUID].contramap(_.juuid)

}
