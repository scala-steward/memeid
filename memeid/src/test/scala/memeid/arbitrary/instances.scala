/*
 * Copyright 2019-2020 Scala Steward <https://github.com/scala-steward>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package memeid.arbitrary

/*
 * Copyright 2019-2020 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import memeid.UUID
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary

object instances {

  implicit val UUID2UUIDArbitraryInstance: Arbitrary[UUID => UUID] = Arbitrary(
    arbitrary[UUID].map(uuid => { _: UUID => uuid })
  )

  implicit val UUIDArbitraryInstance: Arbitrary[UUID] = Arbitrary {
    for {
      msb <- arbitrary[Long]
      lsb <- arbitrary[Long]
    } yield UUID.from(msb, lsb)
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV1ArbitraryInstance: Arbitrary[UUID.V1] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V1]).map(uuid => new UUID.V1(uuid.asJava))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV2ArbitraryInstance: Arbitrary[UUID.V2] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V2]).map(uuid => new UUID.V2(uuid.asJava))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV3ArbitraryInstance: Arbitrary[UUID.V3] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V3]).map(uuid => new UUID.V3(uuid.asJava))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV4ArbitraryInstance: Arbitrary[UUID.V4] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V4]).map(uuid => new UUID.V4(uuid.asJava))
  }

  @SuppressWarnings(Array("scalafix:DisableSyntax.isInstanceOf"))
  implicit val UUIDV5ArbitraryInstance: Arbitrary[UUID.V5] = Arbitrary {
    arbitrary[UUID].retryUntil(_.isInstanceOf[UUID.V5]).map(uuid => new UUID.V5(uuid.asJava))
  }

}
