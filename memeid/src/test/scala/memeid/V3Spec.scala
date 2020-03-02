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

package memeid

import org.specs2.mutable.Specification

class V3Spec extends Specification {

  "V3 constructor" should {

    "create same UUID for the same namespace/name" in {
      val namespace = UUID.V4.random
      val name      = "a-thing"

      val uuid1 = UUID.V3.from(namespace, name)
      val uuid2 = UUID.V3.from(namespace, name)

      uuid1 must be equalTo uuid2
    }

    "create different UUIDs for distinct name" in {
      val namespace = UUID.V4.random
      val name1     = "a-thing"
      val name2     = "b-thing"

      val uuid1 = UUID.V3.from(namespace, name1)
      val uuid2 = UUID.V3.from(namespace, name2)

      uuid1 must not be equalTo(uuid2)
    }

    "create different UUIDs for distinct namespace" in {
      val namespace1 = UUID.V4.random
      val namespace2 = UUID.V4.random
      val name       = "a-thing"

      val uuid1 = UUID.V3.from(namespace1, name)
      val uuid2 = UUID.V3.from(namespace2, name)

      uuid1 must not be equalTo(uuid2)
    }

  }

}
